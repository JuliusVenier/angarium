<script>
    import {onMount} from "svelte";
    import { hashFile, convertArrayBufferToHex} from "$lib/cryptography.js";
    import { createMessage, encrypt } from "openpgp";
    import { isAuthenticated, user,  isDev } from "$lib/user.js";

    import { Icon } from 'svelte-icons-pack';
    import { TrOutlineTrashX } from "svelte-icons-pack/tr";

    let fileInput;
    let isFileSelected = false;
    let fileName;
    let fileExtension;

    let timeAvailable = 1;
    let maxDownloadCnt;

    let usePassword = false;
    let password;

    let downloadID;

    onMount(() => {

    });

    function fileSelected() {
        let file = fileInput.files[0];
        if (file !== null && file !== undefined) {
            let fileNameSplit = file.name.split('.');
            fileName = fileNameSplit[0];
            fileExtension = "." + fileNameSplit[1];

            isFileSelected = true;
        }
    }
    function removeFile() {
        fileInput.value = "";
        fileName = undefined;
        fileExtension = undefined;
        isFileSelected = false;
    }

    async function uploadFile() {
        //TODO check inputs, progress system not finished
        /*if(!checkUploadParams()) {
            console.log("params missing");
            return;
        }*/

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
                passwords: [password.value],
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
                'max-days': timeAvailable
            }
        })
            .then(async (response) => {
                console.log("api/upload/" + fileName, response.status, response.statusText);
                if (response.status === 200) {
                    alert("success");
                    let id = await response.json();
                    if (id !== null && id !== undefined && id.fileId.length > 0) {
                        downloadID = window.location.host + "/download?id=" + id.fileId;
                    }
                }
            })
            .catch((ex) => {
                console.error(ex);
            });
    }

    function copyLink() {
        // TODO test for mobile devices (https://www.w3schools.com/howto/howto_js_copy_clipboard.asp)
        navigator.clipboard.writeText(downloadID);
    }

</script>
<div class="overflow-y-auto">
    <div class="flex-container items-center justify-items-stretch gap-20 h-fit">
        <div class="justify-self-end w-1/4">
            <ul class="steps steps-vertical">
                <li class="step step-accent"></li>
                <li class="step"></li>
                <li class="step"></li>
                <li class="step"></li>
            </ul>
        </div>
        <div class="w-2/4 w-min-70 flex flex-col gap-4">
            <div class="flex flex-row gap-4 items-center">
                <input type="file" class="file-input file-input-bordered file-input-accent" bind:this={fileInput} on:change={fileSelected} />
                <div class="cursor-pointer" class:hidden={!isFileSelected} on:click={removeFile}><Icon src={TrOutlineTrashX} size="32"/></div>
            </div>
            {#if isFileSelected}
                <div class="join">
                    <input type="text" placeholder="Dateiname" class="input input-bordered join-item" bind:value={fileName} />
                    <div class="btn btn-accent join-item">{fileExtension}</div>
                </div>
            {/if}
            <div class="h-4"></div>
            <div class="flex flex-row gap-4 items-center">
                <!--<select class="select select-bordered w-20" bind:value={timeAvailable}>
                    <option value="1">1 Tag</option>
                    <option value="2">2 Tage</option>
                    <option value="4">4 Tage</option>
                    <option value="8">8 Tage</option>
                </select>-->
                <div class="dropdown">
                    <div tabindex="0" class="btn m-1">Tage verfügbar</div>
                    <ul tabindex="0" class="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box">
                        <li><div>1 Tag</div></li>
                        <li><div>2 Tage</div></li>
                        <li><div>4 Tage</div></li>
                        <li><div>8 Tage</div></li>
                    </ul>
                </div>
                <input type="text" class="input input-bordered w-32" placeholder="# Downloads" bind:value={maxDownloadCnt}/>
            </div>
            <div class="h-4"></div>
            <div class="flex flex-row flex-wrap items-center mt-4">
                <input type="checkbox" class="checkbox checkbox-accent" bind:checked={usePassword}/>
                <span class="ml-2">Datei mit Passwort schützen</span>
                {#if usePassword}
                    <div class="basis-full"></div>
                    <input type="text" placeholder="Passwort" class="input input-bordered w-full max-w-xs mt-4" bind:this={password}/>
                {/if}
            </div>
            <button class="btn btn-outline btn-accent mt-20" on:click={uploadFile}>Datei hochladen</button>
            <div class="divider w-4/5"></div>
            <div class="join mt-4 w-full">
                <input type="text" placeholder="Link" class="input input-bordered w-4/6 join-item" readonly bind:value={downloadID}/>
                <button class="btn btn-accent join-item" on:click={copyLink}>Kopieren</button>
            </div>
        </div>
    </div>
</div>
<style>
    .flex-container {
        @apply flex;
        @apply flex-row;
    }

    @media only screen and (max-width: 600px) {
        .flex-container {
            @apply flex;
            @apply flex-col;
        }
    }
</style>