<script>
    import {onMount} from "svelte";
    import { hashFile, convertArrayBufferToHex} from "$lib/cryptography.js";
    import { createMessage, encrypt } from "openpgp";
    import { isAuthenticated, user,  isDev, user_roles } from "$lib/user.js";

    import { Icon } from 'svelte-icons-pack';
    import { TrOutlineTrashX } from "svelte-icons-pack/tr";
    import { AiOutlineUp } from "svelte-icons-pack/ai";
    import { AiOutlineDown } from "svelte-icons-pack/ai";
    import { SlCheck } from "svelte-icons-pack/sl";

    let fileInput;
    let isFileSelected = false;
    let fileName;
    let fileExtension;

    let daysAvailable;
    let maxDownloadCnt;

    let usePassword = false;
    let password;

    let downloadID;

    function fileSelected() {
        if (checkFileInput()) {
            return;
        }

        let file = fileInput.files[0];
        let fileNameSplit = file.name.split('.');
        fileName = fileNameSplit[0];
        fileExtension = "." + fileNameSplit[1];
        isFileSelected = true;
    }
    function removeFile() {
        fileInput.value = "";
        fileName = undefined;
        fileExtension = undefined;
        isFileSelected = false;
    }

    let openDaysDropdown;
    let daysAvailableTxt;
    let daysAvailableInput;
    function changeDaysAvailable(days) {
        daysAvailable = days;
        if (daysAvailableInput === undefined) { return; }
        switch (days) {
            case "1":
                daysAvailableTxt = "1 Tag";
                break;
            case "2":
                daysAvailableTxt = "2 Tage";
                break;
            case "4":
                daysAvailableTxt = "4 Tage";
                break;
            case "8":
                daysAvailableTxt = "8 Tage";
                break;
            default:
                daysAvailableTxt = "Tage verfügbar";
                break;
        }
        daysAvailableInput.innerHTML = daysAvailableTxt;
        openDaysDropdown.removeAttribute("open");
        checkDaysAvailable();
    }

    // checkInputs ------------------------------------------------------

    function checkInputs() {
        checkFileInput();
        checkFileNameInput();
        checkMaxDownload();
        checkDaysAvailable();
        checkPasswordError();

        return fileError || fileNameError || maxDownloadError || daysAvailableError || passwordError;
    }

    let fileError = false;
    function checkFileInput() {
        fileError = fileInput.files === null || fileInput.files === undefined || fileInput.files.length !== 1 ||
            fileInput.files[0] === null || fileInput.files[0] === undefined;
        return fileError;
    }

    let fileNameError = false;
    function checkFileNameInput() {
        if (!isFileSelected) {return;}
        fileNameError = fileName === undefined || fileName === null || fileName === "";
        return fileNameError;
    }

    let maxDownloadError = false;
    function checkMaxDownload() {
        maxDownloadError = maxDownloadCnt === undefined || maxDownloadCnt === null || maxDownloadCnt === "" || isNaN(maxDownloadCnt) || maxDownloadCnt <= "0";
        return maxDownloadError;
    }

    let daysAvailableError = false;
    function checkDaysAvailable() {
        daysAvailableError = daysAvailable === undefined || daysAvailable === null || daysAvailable === "" || isNaN(daysAvailable) || daysAvailable <= "0";
        return daysAvailableError;
    }

    let passwordError= false;
    function checkPasswordError() {
        if (!usePassword) {return;}
        passwordError = password === undefined || password === null || password === "";
        return passwordError;
    }

    // ------------------------------------------------------------------

    async function uploadFile() {
        if(checkInputs()) {
            console.log("params missing");
            return;
        }

        let uploadSuccess = true;
        let file = fileInput.files[0];

        let hash = null;
        try {
            hash = await hashFile(file);
            hash = convertArrayBufferToHex(hash);
        } catch(e) {}
        //console.log("hash: ", hash);

        if (usePassword) {
            //console.log("file", file);
            let buffer = await file.arrayBuffer();
            let fileData = new Uint8Array(buffer);
            //console.log("source array", fileData);

            const message = await createMessage({binary: fileData});
            //console.log("message", message);

            const encrypted = await encrypt({
                message,
                passwords: [password],
                format: 'binary'
            });
            //console.log("encrypted array", encrypted);
            file = new File([encrypted], fileName + fileExtension, {type: 'application/octet-stream'});
        }

        if (uploadSuccess === false) {
            alert("not successful");
            return;
        }

        //Upload API Call
        let url = "api/upload/" + fileName + fileExtension;
        fetch(url, {
            method: "PUT",
            body: file,
            headers: {
                encrypted: usePassword,
                sha256: hash,
                'max-downloads': maxDownloadCnt,
                'max-days': daysAvailable
            }
        })
            .then(async (response) => {
                console.log("api/upload/" + fileName, response.status, response.statusText);
                if (response.status === 200) {
                    await uploadSuccessful(response);
                }
            })
            .catch((ex) => {
                console.error(ex);
            });
    }

    export let scrollContainer;
    let isUploadSuccessful = false;
    async function uploadSuccessful(response) {
        isUploadSuccessful = true;
        let id = await response.json();
        if (id !== null && id !== undefined && id.fileId.length > 0) {
            downloadID = window.location.host + "/download?id=" + id.fileId;
        }
        await scrollContainer.scroll({top: scrollContainer.scrollHeight, behavior: 'smooth'});
    }

    function copyLink() {
        // TODO test for mobile devices (https://www.w3schools.com/howto/howto_js_copy_clipboard.asp)
        navigator.clipboard.writeText(downloadID);
    }

</script>
<div class="w-2/6 min-w-80 flex flex-col gap-4 h-fit my-auto">
    <div class="flex flex-row gap-4 items-center">
        <input type="file" class="file-input file-input-bordered file-input-accent flex-1"
               bind:this={fileInput} on:change={fileSelected}
               class:file-input-error={fileError} disabled={isUploadSuccessful}/>
        {#if !isUploadSuccessful}
            <div class="cursor-pointer" class:hidden={!isFileSelected} on:click={removeFile}><Icon src={TrOutlineTrashX} size="32"/></div>
        {/if}
    </div>
    {#if isFileSelected}
        <div class="join">
            <input type="text" placeholder="Dateiname" class="input input-bordered join-item flex-1"
                   bind:value={fileName} on:input={checkFileNameInput}
                   class:input-error={fileNameError} disabled={isUploadSuccessful}/>
            {#if !isUploadSuccessful}
                <div class="btn btn-accent join-item" class:btn-error={fileNameError}>{fileExtension}</div>
            {:else}
                <div class="btn btn-accent join-item" class:btn-error={fileNameError} disabled>{fileExtension}</div>
            {/if}
        </div>
    {/if}
    <div class="h-4"></div>
    <div class="flex flex-row items-center min-w-80">
        {#if !isUploadSuccessful}
            <details class="dropdown dropdown-hover w-fit min-w-36 mr-2" bind:this={openDaysDropdown}>
                <summary bind:this={daysAvailableInput} tabindex="0" class="btn btn-accent w-full"
                         class:btn-error={daysAvailableError}>Tage verfügbar</summary>
                <ul class="dropdown-content menu p-2 shadow bg-base-100 rounded-box">
                    <li><a class="cursor-pointer" class:bg-accent={daysAvailable === "1"}
                             on:click={() => {changeDaysAvailable("1")}}>1 Tag</a>
                    </li>
                    <li><a class="cursor-pointer" class:bg-accent={daysAvailable === "2"}
                             on:click={() => {changeDaysAvailable("2")}}>2 Tage</a>
                    </li>
                    <li><div class="cursor-pointer" class:bg-accent={daysAvailable === "4"}
                             on:click={() => {changeDaysAvailable("4")}}>4 Tage</div>
                    </li>
                    <li><div class="cursor-pointer" class:bg-accent={daysAvailable === "8"}
                             on:click={() => {changeDaysAvailable("8")}}>8 Tage</div>
                    </li>
                </ul>
            </details>
            {:else}
            <div class="btn w-fit min-w-36 mr-2 btn-accent" disabled>{daysAvailableTxt}</div>
        {/if}
        <div class="join">
            <input type="text" class="input input-bordered min-w-20 w-32 join-item text-center" placeholder="# Downloads"
                   bind:value={maxDownloadCnt} on:input={checkMaxDownload}
                   class:input-error={maxDownloadError} disabled={isUploadSuccessful}/>
            {#if maxDownloadError}
                <div class="bg-error join-item" style="width: 40px"></div>
            {:else if isUploadSuccessful}
                <div class="bg-black opacity-20 join-item" style="width: 40px"></div>
            {:else}
                <div class="flex flex-col items-center justify-center px-3 bg-accent text-white join-item gap-1">
                    <div class="cursor-pointer" on:click={() => {maxDownloadCnt++; checkMaxDownload();}}>
                        <Icon src={AiOutlineUp}/>
                    </div>
                    <div class="cursor-pointer" on:click={() => {maxDownloadCnt--; checkMaxDownload();}}>
                        <Icon src={AiOutlineDown}/>
                    </div>
                </div>
            {/if}
        </div>
    </div>
    <div class="h-4"></div>
    <div class="flex flex-row flex-wrap items-center mt-4">
        <input type="checkbox" class="checkbox checkbox-accent" bind:checked={usePassword} disabled={isUploadSuccessful}/>
        <span class="ml-2">Datei mit Passwort schützen</span>
        {#if usePassword}
            <div class="basis-full"></div>
            <input type="text" placeholder="Passwort" class="input input-bordered w-full mt-4"
                   bind:value={password} on:input={checkPasswordError}
                   class:input-error={passwordError} disabled={isUploadSuccessful}/>
        {/if}
    </div>
    {#if !isUploadSuccessful}
        <button class="btn btn-outline btn-accent mt-20" on:click={uploadFile}>Datei hochladen</button>
    {:else}
        <div class="btn btn-success mt-20">
            <Icon src={SlCheck} size="32"/>
            Datei hochladen erfolgreich
        </div>
        <div class="divider min-w-80 flex-1"></div>
        <div class="join mt-4 w-full min-w-80">
            <input type="text" placeholder="Link" class="input input-bordered flex-1 join-item" readonly bind:value={downloadID}/>
            <button class="btn btn-accent join-item" on:click={copyLink}>Kopieren</button>
        </div>
        <button class="btn btn-accent btn-outline" on:click={() => {window.location.reload();}}>Weitere Datei hochladen</button>
    {/if}
</div>
{#if $isDev}
    <div id="dev-tools" class="fixed right-4 top-20 flex flex-col gap-2">
        <div class="flex flex-row flex-wrap items-center justify-end">
            <span>isFileSelected</span>
            <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={isFileSelected}/>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end">
            <span>usePassword</span>
            <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={usePassword}/>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end">
            <span>isUploadSuccessful</span>
            <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={isUploadSuccessful}/>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end"></div>
        <div class="flex flex-row flex-wrap items-center justify-end">
            <span>fileError</span>
            <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={fileError}/>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end">
            <span>fileNameError</span>
            <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={fileNameError} disabled={!isFileSelected}/>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end">
            <span>daysAvailableError</span>
            <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={daysAvailableError}/>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end">
            <span>maxDownloadError</span>
            <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={maxDownloadError}/>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end">
            <span>passwordError</span>
            <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={passwordError} disabled={!usePassword}/>
        </div>
    </div>
{/if}