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
    let timeAvailable;
    let maxDownloadCnt;

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
        console.log("hash: ", hash);

        if (usePassword) {
            // - Web Crypto API --------------
            await encryptFile(file, password)
                .then(encryptedFile => {
                    console.log("encryptFile success", encryptedFile);
                    file = encryptedFile;
                })
                .catch(ex => {
                    console.error(ex);
                    uploadSuccess = false;
                });
            //--------------------------------
            // - openpgp ---------------------
            /*
            console.log("file", file);
            let stream = file.stream();
            const message = await createMessage({binary: stream});
            console.log("message", message);
            const encrypted = await encrypt({
                message,
                passwords: [password],
                format: 'binary'
            });
            const encryptedMessage = await readMessage({
                binaryMessage: encrypted
            });
            const { data: decrypted } = await decrypt({
                message: encryptedMessage,
                passwords: [password], // decrypt with password
                format: 'binary' // output as Uint8Array
            });
            console.log(decrypted);
            let decryptedFile = new File(decrypted, "decryptedFile.pdf", {type: 'application/pdf'})
            console.log("decrypted", decryptedFile);
            */
            //--------------------------------
        }
        console.log(file);
        console.log(await convertArrayBufferToHex(await hashFile(file)));

        if (uploadSuccess === false) {
            alert("not successful");
            return;
        }

        //Upload API Call
        let url = "api/upload/" + nameInput.value;
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
        .then((response) => {
            console.log("api/upload/" + nameInput.value, response.status);
            alert("success");
        })
        .catch((ex) => {
            console.error(ex);
        });
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
                        <option value="1">1h</option>
                        <option value="4">4h</option>
                        <option value="8">8h</option>
                        <option value="12">12h</option>
                        <option value="24">24h</option>
                        <option value="48">48h</option>
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
                    <input type="text" placeholder="Link" class="input input-bordered w-4/6 join-item" readonly />
                    <button class="btn btn-accent join-item">Kopieren</button>
                </div>
            </div>
        </div>
        <div id="bottom-spacer" class="h-3/4"></div>
    </div>
</div>