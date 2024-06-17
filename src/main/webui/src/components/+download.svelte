<script>
    import Label from "../components/shared/info/+label.svelte"

    import { onMount } from "svelte";
    import { convertArrayBufferToHex, hashFile, decryptFile } from "$lib/cryptography.js";

    export let id;
    let validID = undefined;
    let isEncrypted = false;
    let maxDownloadsReached = false;
    let canDownload = false;
    let password;

    let filename;
    let fileHash;

    onMount(checkFileID);

    function checkFileID() {
        if (id === null || id === undefined || id.length === 0) {
            validID = undefined;
            return;
        }

        validID = false;
        filename = undefined;
        canDownload = false;
        fetch("api/meta-data/" + id, {
            method: "GET"
        })
            .then(async (response) => {
                console.log("api/meta-data/" + id, response.status, response.statusText);
                if(response.status === 200) {
                    let body = await response.json();
                    validID = true;
                    filename = body.name;
                    fileHash = body.sha256;
                    isEncrypted = body.encrypted;
                    maxDownloadsReached = body.maxDownloads <= body.currentDownloads;

                    if (!isEncrypted && !maxDownloadsReached) {
                        canDownload = true;
                    }
                }
            })
            .catch((error) => {
                console.error(error);
            });
    }

    function checkPasswordInput() {
        canDownload = isEncrypted && password !== null && password !== undefined && password.length > 0;
    }

    async function downloadClick() {
        await fetch("api/download/" + id, {
            method: "GET"
        })
            .then(async response => {
                console.log("api/download/" + id, response.status);
                if (response.status !== 200) {
                    throw new Error("Unexpected response");
                }

                let blob = await response.blob();
                let file = new File([blob], filename + ".pdf", {type: 'application/pdf'});

                if (isEncrypted) {
                    file = await decryptFile(file, password);
                }

                let hash = convertArrayBufferToHex(await hashFile(file));
                if (checkHash(fileHash, hash)) {
                    let url = URL.createObjectURL(file);
                    downloadFile(url, file.name);
                }
            })
            .catch(ex => {
                console.error(ex);
            });

        checkFileID();
        checkPasswordInput();
    }

    function checkHash(hashBeforeEncryption, currentHash) {
        if (hashBeforeEncryption !== currentHash) {
            console.error("Hash stimmt nicht überein!");
            return false;
        }
        return true;
    }

    function downloadFile(uri, name) {
        let link = document.createElement("a");
        link.download = name;
        link.href = uri;
        link.click();
    }
</script>
<div class="flex flex-col h-full w-full items-center justify-center">
    <div class="flex flex-col w-1/5 gap-4">
        <div class="flex flex-row gap-4 items-center w-full">
            <input type="text" placeholder="Download ID" class="input input-bordered w-full" bind:value={id} on:input={checkFileID}/>
        </div>
        {#if isEncrypted && !maxDownloadsReached}
            <input type="password" placeholder="Passwort" class="input input-bordered w-full" bind:value={password} on:input={checkPasswordInput}/>
        {/if}
        {#if canDownload}
            <button class="btn btn-outline btn-accent" on:click={downloadClick}>Datei herunterladen</button>
        {:else}
            <button class="btn btn-outline btn-disabled">Datei herunterladen</button>
        {/if}
        <div class="flex flex-row items-center gap-4">
            {#if filename !== undefined}
                <span>{filename}</span>
            {/if}
            {#if validID !== undefined}
                <div>
                    {#if !validID}
                        <Label type={"error"} text={"File existiert nicht"} showIcon={true}  />
                    {:else if maxDownloadsReached}
                        <Label type={"error"} text={"Maximale downloads erreicht!"} showIcon={true}  />
                    {:else if isEncrypted && !canDownload}
                        <Label type={"warning"} text={"Passwort erforderlich"} showIcon={true}  />
                    {:else if validID}
                        <Label type={"success"} text={"File existiert"} showIcon={true}  />
                    {/if}
                </div>
            {/if}
        </div>
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
            <span>maxDownloadsReached</span>
            <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={maxDownloadsReached}/>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end">
            <span>canDownload</span>
            <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" bind:checked={canDownload}/>
        </div>
    </div>
{/if}