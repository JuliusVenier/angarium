<style>

    .scroll-container {
        overflow-y: auto;
        @apply snap-y;
    }

    .section-header {
        @apply mb-10;
        @apply font-bold;
        @apply text-xl;
    }

    .section-content {
        @apply ml-8;
    }

</style>
<script>
    import {onMount} from "svelte";
    import { encryptFile, hashFile, convertArrayBufferToHex, decryptFile} from "$lib/cryptography.js";
    import { createMessage, encrypt, decrypt, readMessage } from "openpgp";

    const fileID = "section_fileSelect";
    const nameID = "section_filename";
    const variousID = "section_various";
    const linkID = "section_link";
    const fileTxt = "Datei auswählen";
    const nameTxt = "Name vergeben";
    const variousTxt = "Zusätzliche Einstellungen";
    const linkTxt = "Link";

    let fileInput;
    let nameInput;
    let fileExtension;
    let usePassword = false;
    let password;
    let sendToUsers = false;
    let timeAvailable = 1;
    let maxDownloadCnt;

    let downloadID;

    onMount(() => {
        updateProgress();
    });

    function fileSelected() {
        let file = fileInput.files[0];
        if (file !== null && file !== undefined) {
            console.log(file);
            let fileNameSplit = file.name.split('.');
            nameInput.value = fileNameSplit[0];
            fileExtension = "." + fileNameSplit[1];
        }
    }

    let progress = ["empty", "empty", "empty", "empty" ];
    function updateProgress() {
        if (fileInput.files.length > 0) {
            progress[0] = "done";
        }
        else {
            progress[0] = "empty";
        }

        if (nameInput.value.length > 0) {
            progress[1] = "done";
        }
        else {
            progress[1] = "empty";
        }

        if (!usePassword || (usePassword && password.value.length > 0)) {
            progress[2] = "done";
        }
        else {
            progress[2] = "empty";
        }

        if (!sendToUsers) {
            progress[3] = "done";
        }
        else {
            progress[3] = "empty";
        }

        //console.log(progress);
    }

    function checkUploadParams() {
        return !progress.includes("empty");
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
            file = new File([encrypted], nameInput.value + fileExtension, {type: 'application/octet-stream'});
        }

        if (uploadSuccess === false) {
            alert("not successful");
            return;
        }

        //Upload API Call
        let url = "api/upload/" + nameInput.value + fileExtension;
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
            console.log("api/upload/" + nameInput.value, response.status, response.statusText);
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
<div class="flex h-full scroll-container">
    <div class="w-1/4 absolute mt-10 pl-10">
        <ul class="steps steps-vertical">
            {#if progress[0] === "done"}
                <li class="step step-accent"><a href="#{fileID}">{fileTxt}</a></li>
            {:else}
                <li class="step"><a href="#{fileID}">{fileTxt}</a></li>
            {/if}
            {#if progress[1] === "done"}
                <li class="step step-accent"><a href="#{nameID}">{nameTxt}</a></li>
            {:else}
                <li class="step"><a href="#{nameID}">{nameTxt}</a></li>
            {/if}
            {#if progress[2] === "done"}
                <li class="step step-accent"><a href="#{variousID}">{variousTxt}</a></li>
            {:else}
                <li class="step"><a href="#{variousID}">{variousTxt}</a></li>
            {/if}
            {#if progress[3] === "done"}
                <li class="step step-accent"><a href="#{linkID}">{linkTxt}</a></li>
            {:else}
                <li class="step"><a href="#{linkID}">Fertig</a></li>
            {/if}
        </ul>
    </div>
    <div class="w-1/4"></div>
    <div class="w-3/4">
        <div id="{fileID}" class="pt-10 snap-start">
            <div class="section-content">
                <input type="file" class="file-input file-input-bordered file-input-accent w-full max-w-xs" bind:this={fileInput} on:change={fileSelected} />
            </div>
        </div>
        <div id="{nameID}" class="pt-10 snap-start">
            <div class="section-content">
                <div class="join">
                    <input type="text" placeholder="Dateiname" class="input input-bordered w-max join-item" bind:this={nameInput} on:change={updateProgress} />
                    <div class="btn btn-accent join-item">{fileExtension}</div>
                </div>
            </div>
        </div>
        <div id="{variousID}" class="pt-10 snap-start">
            <div class="section-header">{variousTxt}</div>
            <div class="section-content">
                <div class="flex flex-row flex-wrap items-center">
                    <select class="select select-bordered w-20 max-w-xs" bind:value={timeAvailable}>
                        <option value="1">1 Tag</option>
                        <option value="2">2 Tage</option>
                        <option value="4">4 Tage</option>
                        <option value="8">8 Tage</option>
                    </select>
                    <div class="w-full"></div>
                    <span class="ml-2 text-gray">Die Datei ist für {timeAvailable} Stunden verfügbar</span>
                </div>
                <div class="flex flex-row flex-wrap items-center mt-4">
                    <span class="mr-2"># Downloads</span>
                    <input type="text" class="input input-bordered w-20 max-w-xs" bind:value={maxDownloadCnt} on:change={updateProgress} />
                    {#if maxDownloadCnt !== undefined && maxDownloadCnt !== ""}
                        <div class="w-full"></div>
                        <span class="ml-2">Die Datei kann maximal {maxDownloadCnt} x heruntergeladen werden </span>
                    {/if}
                </div>
                <div class="flex flex-row flex-wrap items-center mt-4">
                    <input type="checkbox" class="checkbox checkbox-accent" bind:checked={usePassword} on:change={updateProgress} />
                    <span class="ml-2">Datei mit Passwort schützen</span>
                    {#if usePassword}
                        <div class="basis-full"></div>
                        <input type="text" placeholder="Passwort" class="input input-bordered w-full max-w-xs mt-4" bind:this={password} on:change={updateProgress} />
                    {/if}
                </div>
            </div>
        </div>
        <button class="btn btn-outline btn-accent mt-20" on:click={uploadFile}>Datei hochladen</button>
        <div class="divider w-4/5"></div>
        <div id="{linkID}" class="pt-10 snap-start">
            <div class="section-header">{linkTxt}</div>
            <div class="section-content">
                <div class="join mt-4 w-full">
                    <input type="text" placeholder="Link" class="input input-bordered w-4/6 join-item" readonly bind:value={downloadID}/>
                    <button class="btn btn-accent join-item" on:click={copyLink}>Kopieren</button>
                </div>
            </div>
        </div>
        <div id="bottom-spacer" class="h-3/4"></div>
    </div>
</div>