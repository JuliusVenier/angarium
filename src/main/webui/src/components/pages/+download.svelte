<script>
    import Label from "../shared/info/+label.svelte"
    import {onMount} from "svelte";
    import {isDev} from "$lib/user.js";
    import {convertArrayBufferToHex, hashFile} from "$lib/cryptography.js";
    import {decrypt, readMessage} from "openpgp";
    import {pushPopup, popupColor} from "$lib/popup.js";

    // Icon imports ---------------------------------------
    import {Icon} from 'svelte-icons-pack';
    import {SlCheck, SlClose} from "svelte-icons-pack/sl";
    import {RiSystemErrorWarningLine} from "svelte-icons-pack/ri";
    //-----------------------------------------------------

    export let id;
    let validID = undefined;
    let isEncrypted = false;
    let maxDownloadsReached = false;
    let canDownload = false;
    let password;

    let filename;
    let fileHash;

    onMount(checkFileID);

    function resetVariables() {
        validID = undefined;
        isEncrypted = false;
        maxDownloadsReached = false;
        canDownload = false;
        password = undefined;
        filename = undefined;
        fileHash = undefined;
    }

    function checkFileID() {
        resetVariables();
        if (id === null || id === undefined || id.length === 0) {
            return;
        }

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

    let successfulDownload = false;
    async function downloadClick() {
        await fetch("api/download/" + id, {
            method: "GET"
        })
            .then(async response => {
                console.log("api/download/" + id + " [GET]", response.status, response.statusText);
                if (response.status !== 200) {
                    throw new Error("Unexpected response");
                }

                let blob = await response.blob();
                let file = new File([blob], filename, {type: 'application/octet-stream'});

                if (isEncrypted) {
                    let buffer = await file.arrayBuffer();
                    let fileData = new Uint8Array(buffer);
                    const message = await readMessage({
                        binaryMessage: fileData
                    });

                    try {
                        const {data: decrypted} = await decrypt({
                            message: message,
                            passwords: [password],
                            format: 'binary'
                        });

                        file = new File([decrypted], filename, {type: 'application/octet-stream'});
                    }
                    catch (ex) {
                        console.error(ex);
                        pushPopup("Das eingegebene Passwort ist Falsch!", popupColor.error);
                    }
                }

                let hash = convertArrayBufferToHex(await hashFile(file));
                if (checkHash(fileHash, hash)) {
                    let url = URL.createObjectURL(file);
                    downloadFile(url, file.name);
                    pushPopup("Die Datei wurde erfolgreich heruntergeladen.", popupColor.success);
                    successfulDownload = true;
                }
                else {
                    throw new Error("wrong Hash");
                }
            })
            .catch(ex => {
                console.error(ex);
                pushPopup("Beim herunterladen der Datei ist ein Fehler aufgetreten!", popupColor.error);
            });

        if (successfulDownload) {
            setTimeout(async () => {
                id = null;
                await checkFileID();
                checkPasswordInput();
                successfulDownload = false;
            }, 1000)
        }
        else {
            checkFileID();
            checkPasswordInput();
        }
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
<div class="flex flex-col h-fit my-auto w-full items-center">
    <div class="flex flex-col w-1/5 gap-4 min-w-80">
        <div class="flex flex-row gap-4 items-center w-full">
            <input type="text" placeholder="Download ID" class="input input-bordered w-full"
                   bind:value={id} on:input={checkFileID} disabled={successfulDownload}/>
        </div>
        {#if isEncrypted && !maxDownloadsReached}
            <input type="password" placeholder="Passwort" class="input input-bordered w-full"
                   bind:value={password} on:input={checkPasswordInput} disabled={successfulDownload}/>
        {/if}
        <button class="btn btn-outline btn-accent" disabled={!canDownload || successfulDownload} on:click={downloadClick}>Datei herunterladen</button>
        <div class="flex flex-row items-center gap-4" class:hidden={filename === undefined && validID === undefined}>
            {#if filename !== undefined}
                <span>{filename}</span>
            {/if}
            {#if validID !== undefined}
                <div>
                    {#if !validID}
                        <Label color={"error"} text={"File existiert nicht"}>
                            <Icon src={SlClose} size="32" slot="icon"/>
                        </Label>
                    {:else if maxDownloadsReached}
                        <Label color={"error"} text={"Maximale downloads erreicht!"}>
                            <Icon src={SlClose} size="32" slot="icon"/>
                        </Label>
                    {:else if isEncrypted && !canDownload}
                        <Label color={"warning"} text={"Passwort erforderlich"}>
                            <Icon src={RiSystemErrorWarningLine} size="32" slot="icon"/>
                        </Label>
                    {:else if validID}
                        <Label color={"success"} text={"File existiert"}>
                            <Icon src={SlCheck} size="32" slot="icon"/>
                        </Label>
                    {/if}
                </div>
            {/if}
        </div>
    </div>
</div>
{#if $isDev}
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