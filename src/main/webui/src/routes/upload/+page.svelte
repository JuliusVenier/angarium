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
    import {encryptFile, hashFile, convertArrayBufferToHex } from "$lib/cryptography.js";

    //Testing imports
    import { decryptFile, hashArrayBuffer } from "$lib/cryptography.js";


    const fileID = "file";
    const nameID = "name";
    const securityID = "sec";
    const sharingID = "sha";
    const fileTxt = "Datei auswählen";
    const nameTxt = "Name vergeben";
    const securityTxt = "Sicherheit";
    const sharingTxt = "Sharing Methode";

    let fileInput;
    let nameInput;
    let usePassword = false;
    let password;
    let sendToUsers = false;
    let timeAvailable;
    let maxDownloadCnt;

    onMount(() => {
        updateProgress();
    });

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

        let file = fileInput.files[0];

        console.log("start hash");
        let hash = null;
        try {
            hash = await hashFile(file);
            hash = convertArrayBufferToHex(hash);
        } catch(e) {}
        console.log("end hash: ", hash);

        if (usePassword) {
            console.log("start encrypt");
            try {
                file = await encryptFile(file, password);
                if (file === null || file === undefined) {
                    console.log("encryption failed");
                    return;
                }
            } catch(e) {}
            console.log("end encrypt: ", file);

            // decrypt test -----------------
            /*
            let encHash = await hashArrayBuffer(file);
            encHash = convertArrayBufferToHex(encHash);
            console.log("encHash: ", encHash);

            let decrypted = await decryptFile(file, password);
            let decHash = await hashArrayBuffer(decrypted);
            decHash = convertArrayBufferToHex(decHash);
            console.log("decHash: ", decHash);
            */
            // -------------------------------
        }

        //Upload API Call
        let url = "/api/upload/" + nameInput.value;
        fetch(url, {
            method: "PUT",
            body: file,
            headers: {
                "max-days": timeAvailable,
                "max-downloads": maxDownloadCnt,
                "sha256": hash
            }
        })
        .then((response) => {
            console.log(response.status);
            alert("success");
        })
        .catch((error) => {
            console.error(error);
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
                <li class="step step-accent"><a href="#{securityID}">{securityTxt}</a></li>
            {:else}
                <li class="step"><a href="#{securityID}">{securityTxt}</a></li>
            {/if}
            {#if progress[3] === "done"}
                <li class="step step-accent"><a href="#{sharingID}">{sharingTxt}</a></li>
            {:else}
                <li class="step"><a href="#{sharingID}">{sharingTxt}</a></li>
            {/if}
        </ul>
    </div>
    <div class="w-1/4"></div>
    <div class="w-3/4">
        <div id="{fileID}" class="pt-10 snap-start">
            <div class="section-content">
                <input type="file" class="file-input file-input-bordered file-input-accent w-full max-w-xs" bind:this={fileInput} on:change={updateProgress} />
            </div>
        </div>
        <div id="{nameID}" class="pt-10 snap-start">
            <div class="section-content">
                <div class="join">
                    <input type="text" placeholder="Dateiname" class="input input-bordered w-full max-w-xs join-item" bind:this={nameInput} on:change={updateProgress} />
                    <div class="btn btn-accent join-item">.pdf</div>
                </div>
            </div>
        </div>
        <div id="{securityID}" class="pt-10 snap-start">
            <div class="section-header">{securityTxt}</div>
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
        <div id="{sharingID}" class="pt-10 snap-start">
            <div class="section-header">{sharingTxt}</div>
            <div class="section-content">
                <div class="flex flex-row flex-wrap items-center">
                    <span>nur über Link</span>
                    <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={sendToUsers} on:change={updateProgress} />
                    <span>direkt an Benutzer senden</span>
                </div>
                <div class="join mt-4 w-full">
                    <input type="text" placeholder="Link" class="input input-bordered w-4/5 join-item" />
                    <button class="btn btn-accent join-item">Kopieren</button>
                </div>
            </div>
        </div>
        <div class="divider"></div>
        <button class="btn btn-outline btn-accent" on:click={uploadFile}>Datei hochladen</button>
        <div class="h-3/5"></div>
    </div>
</div>