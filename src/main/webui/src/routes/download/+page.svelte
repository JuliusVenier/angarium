<script>
    import { convertArrayBufferToHex, hashFile, decryptFile } from "$lib/cryptography.js";

    let id;
    let validID = undefined;
    let isEncrypted = false;
    let canDownload = false;
    let password;

    function checkFileID() {

    }

    function downloadFile() {
        //TODO fetch to download check if file exists and if password is needed
        fetch("api/download" + id, {
            method: "GET"
        })
            .then((response) => {
                console.log(response.status);
                let file = response.file;

                // decrypt file
                if (isEncrypted) {
                    file = decryptFile(file, password);
                }

                // hash check
                let hash = convertArrayBufferToHex(hashFile(file));
                if (hash !== response.sha256) {
                    console.error("Hash stimmt nicht überein!");
                }
            })
            .catch((error) => {
                console.error(error);
            });
    }
</script>
<div class="flex flex-col h-full w-full items-center justify-center">
    <div class="flex flex-col gap-4">
        <div class="flex flex-row gap-4 items-center w-full">
            <input type="text" placeholder="Download ID" class="input input-bordered w-full max-w-xs" bind:value={id} on:input={checkFileID}/>
            {#if validID !== undefined}
                <div>
                {#if validID && !isEncrypted}
                    <div class="alert alert-success h-12 w-max text-l flex flex-row justify-center">
                        <div>
                            <div class="w-3 h-1.5 rounded-sm bg-success-content rotate-45"></div>
                            <div class="w-6 h-1.5 -mt-2 ml-1 rounded-sm bg-success-content -rotate-45"></div>
                        </div>
                        <span>ID existiert</span>
                    </div>
                {:else if validID && isEncrypted}
                    <div class="alert alert-warning h-12 w-max text-l flex flex-row justify-center">
                        <div>
                            <div class="w-2 h-4 rounded-sm bg-warning-content"></div>
                            <div class="w-2 h-2 mt-0.5 rounded-sm bg-warning-content"></div>
                        </div>
                        <span>Passwort erforderlich</span>
                    </div>
                {:else if validID === false}
                    <div class="alert alert-error h-12 w-max text-l flex flex-row justify-center">
                        <div class="flex flex-row">
                            <div class="w-6 h-1.5 rounded-sm bg-error-content rotate-45"></div>
                            <div class="w-6 h-1.5 -ml-6 rounded-sm bg-error-content -rotate-45"></div>
                        </div>
                        <span>ID existiert nicht</span>
                    </div>
                {/if}
            </div>
            {/if}
        </div>
        {#if isEncrypted}
            <input type="password" placeholder="Passwort" class="input input-bordered w-full" bind:value={password}/>
        {/if}
        {#if canDownload}
            <button class="btn btn-outline btn-accent" on:click={downloadFile}>Datei herunterladen</button>
        {:else}
            <button class="btn btn-outline btn-disabled">Datei herunterladen</button>
        {/if}
    </div>
</div>
{#if true}
<div id="dev-tools" class="fixed right-4 top-20 flex flex-col gap-2">
    <div class="flex flex-row flex-wrap items-center justify-end gap-2">
        <span>validID</span>
        <button class="btn w-20" on:click={() => { if (validID === undefined) { validID = true; } else if (validID === true) { validID = false;} else {validID = undefined;}}}>{validID}</button>
    </div>
    <div class="flex flex-row flex-wrap items-center justify-end">
        <span>isEncrypted</span>
        <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={isEncrypted}/>
    </div>
    <div class="flex flex-row flex-wrap items-center justify-end">
        <span>canDownload</span>
        <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={canDownload}/>
    </div>
</div>
{/if}