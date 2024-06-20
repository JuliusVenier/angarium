<script>
    import { onMount } from "svelte";
    import { isAuthenticated, user, isDev, user_roles } from "$lib/user.js";
    import FilterList from '../shared/+filterList.svelte';

    // Icon imports ---------------------------------------
    import { Icon } from 'svelte-icons-pack';
    import { TrOutlineTrashX } from "svelte-icons-pack/tr";
    import { CgSoftwareUpload } from "svelte-icons-pack/cg";
    import { AiOutlineLink } from "svelte-icons-pack/ai";
    import { TrOutlineLock } from "svelte-icons-pack/tr";
    import { CgSoftwareDownload } from "svelte-icons-pack/cg";
    //-----------------------------------------------------

    const dateOptions = { year: "numeric", month: "2-digit", day: "2-digit" };

    let fileList = [];
    let scrollElement;

    onMount(() => {
        fetchFileList();
    });

    function fetchFileList() {
        fetch("api/meta-data/me/files", {
            method: "GET"
        })
            .then(async (response) => {
                console.log("api/meta-data/me/files", response.status, response.statusText);
                if (response.status === 200) {
                    fileList = await response.json();
                }
            })
            .catch((ex) => {
                console.error(ex);
            });
    }

    function getDownloadLink(row) {
        let downloadLink = window.location.host + "/download?id=" + row.id;
        navigator.clipboard.writeText(downloadLink);

        let element = event.currentTarget;
        console.log(element);
        element.setAttribute("data-tip", "Link in Zwischenablage kopiert!");
        setTimeout(() => {
            element.setAttribute("data-tip", "Download Link kopieren");
        }, 1000);
    }

    function deleteFile(row) {
        if (confirm("Wollen Sie das Dokument '" + row.name + "' wirklich löschen?")) {
            fetch("api/meta-data/" + row.id, {
                method: "DELETE"
            })
                .then((response) => {
                    if (response.status === 204) {
                        alert("deleted");
                    } else {
                        alert("error");
                    }
                })
                .catch((ex) => {
                    console.log(ex);
                    alert("error");
                });
        }
    }

</script>
<div bind:this={scrollElement} class="h-full w-full overflow-y-auto">
    <FilterList
            staticTable={true}
            data={fileList}
            field="name"
            let:item={row}
    >
        <colgroup slot="colgroup">
            <col width="auto"/>
            <col width="100px"/>
            <col width="100px"/>
            <col width="60px"/>
            <col width="54px"/>
            <col width="40px"/>
            <col width="54px"/>
            <col width="54px"/>
        </colgroup>
        <tr class="header" slot="header">
            <th class="filename"><div class="h-14 align-center">Name</div></th>
            <th class="uploadDate"><div class="tooltip tooltip-bottom" data-tip="hochgeladen am"><Icon src={CgSoftwareUpload} size="32" /></div></th>
            <th class="deleteDate"><div class="tooltip tooltip-bottom" data-tip="Löschung am"><Icon src={TrOutlineTrashX} size="32" /></div></th>
            <th class="downloads"><div class="tooltip tooltip-bottom" data-tip="Anzahl Downloads / maximale Downloads"><Icon src={CgSoftwareDownload} size="32" /></div></th>
            <th class="encrypted"></th>
            <th></th>
            <th><div><button class="btn btn-outline btn-accent p-2 min-h-0 h-fit tooltip tooltip-top tooltip-accent" data-tip="Datei hochladen" on:click={() => { window.location.href="/upload" }}><Icon src={CgSoftwareUpload} size="32"/></button></div></th>
            <th></th>
        </tr>
        <tr class="row" style="height: 200px" slot="empty" class:hidden={fileList !== null && fileList !== undefined && fileList.length > 0}>
            <td colspan="8">
                <div class="flex flex-col items-center justify-center gap-4 w-full">
                    <span>Zur Zeit sind keine Dateien vorhanden</span>
                    <button class="btn btn-outline btn-accent p-2 min-h-0 h-fit" on:click={() => { window.location.href="/upload" }}>Datei hochladen</button>
                </div>
            </td>
        </tr>
        <tr id="rowID_{row.id}" class="row">
            <td class="filename">{row.name}</td>
            <td>{new Date(row.creationDate).toLocaleDateString("de-DE", dateOptions)}</td>
            <td>{new Date(row.deletionDate).toLocaleDateString("de-DE", dateOptions)}</td>
            <td>{row.currentDownloads} / {row.maxDownloads}</td>
            <td><div class="tooltip tooltip-bottom" class:invisible={!row.encrypted} data-tip="Passwort geschützt"><Icon src={TrOutlineLock} size="32" /></div></td>
            <td></td>
            <td><button class="btn btn-outline p-2 min-h-0 h-fit tooltip tooltip-bottom" data-tip="Download Link kopieren" on:click={getDownloadLink(row)}><Icon src={AiOutlineLink} size="32" /></button></td>
            <td><button class="btn btn-outline btn-error p-2 min-h-0 h-fit tooltip tooltip-bottom tooltip-error" data-tip="Datei löschen" on:click={deleteFile(row)}><Icon src={TrOutlineTrashX} size="32" /></button></td>
        </tr>
    </FilterList>
    <div class="spacer-row h-28"></div>
</div>
<style>
    .header {
        @apply font-bold;
        @apply border-b;
        @apply absolute;
        @apply top-0;
        @apply sticky;
        @apply bg-white;
        @apply z-10;
        @apply h-24;
        @apply align-bottom;
    }

    .header > th > div {
        @apply flex;
        @apply justify-center;
        @apply items-center;
        @apply h-14;
    }

    .row {
        @apply h-14;
        @apply text-center;
    }

    .row:hover {
        @apply border-accent;
    }

</style>
{#if $isDev}
    <div id="dev-tools" class="fixed right-4 top-20 flex flex-col gap-2">
        <div class="flex flex-row flex-wrap items-center justify-end gap-2">
            <button class="btn w-20" on:click={fetchFileList}>fetchRows</button>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end gap-2">
            <button class="btn w-20" on:click={() => { if(fileList !== undefined) { fileList = [...fileList, fileList.at(-1)]; } }}>addRow</button>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end gap-2">
            <button class="btn w-20" on:click={() => { fileList.pop(); fileList = fileList; }}>popRow</button>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end gap-2">
            <button class="btn w-20" on:click={() => { fileList = []; }}>deleteRows</button>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end gap-2">
            <button class="btn w-20" on:click={() => { fileList = fileList.concat(fileList); }}>dupeRows</button>
        </div>
    </div>
{/if}