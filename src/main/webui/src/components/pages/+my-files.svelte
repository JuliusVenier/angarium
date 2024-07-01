<svelte:head>
    <title>angarium - Meine Dateien</title>
</svelte:head>
<script>
    import {onMount} from "svelte";
    import {isDev} from "$lib/user.js";
    import FilterList from '../shared/+filterList.svelte';
    import {goto} from '$app/navigation';
    import {pushPopup, popupColor} from "$lib/popup.js";

    // Icon imports ---------------------------------------
    import {Icon} from 'svelte-icons-pack';
    import {TrOutlineLock, TrOutlineTrashX} from "svelte-icons-pack/tr";
    import {CgSoftwareDownload, CgSoftwareUpload} from "svelte-icons-pack/cg";
    import {AiOutlineLink} from "svelte-icons-pack/ai";
    import Modal from "../shared/info/+modal.svelte";
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

    function deleteFile() {
        let id = confirmUserID;
        closeConfirm();
        closeConfirm();
        fetch("api/meta-data/me/" + id, {
            method: "DELETE"
        })
            .then((response) => {
                console.log("api/meta-data/me/" + id + " [DELETE]", response.status, response.statusText);
                if (response.status === 204) {
                    pushPopup("Datei wurde erfolgreich gelöscht.", popupColor.success, 2500);
                    window.location.reload();
                } else {
                    throw new Error("Unexpected Response: " + response.status + " (" + response.statusText + ")");
                }
            })
            .catch((ex) => {
                console.error(ex);
                pushPopup("Beim löschen der Datei ist ein Fehler aufgetreten!", popupColor.error);
            });
    }

    let showConfirm = false;
    let confirmDialog;
    let confirmUserID = undefined;

    function openConfirm(userID) {
        showConfirm = true;
        confirmUserID = userID;
    }
    function closeConfirm() {
        confirmDialog.close();
        showConfirm = false;
        confirmUserID = undefined;
    }

</script>
<div bind:this={scrollElement} class="h-full w-full overflow-y-auto">
    <FilterList
            staticTable={true}
            data={fileList}
            field="name"
            let:item={row}
            searchPlaceholder="Name"
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
            <th>
                <div>
                    <button class="btn btn-outline btn-accent p-2 min-h-0 h-fit tooltip tooltip-top tooltip-accent"
                            data-tip="Datei hochladen" on:click={() => { goto('/upload'); }}
                    >
                        <Icon src={CgSoftwareUpload} size="32"/>
                    </button>
                </div>
            </th>
            <th></th>
        </tr>
        <tr class="row" style="height: 200px" slot="empty"
            class:hidden={fileList !== null && fileList !== undefined && fileList.length > 0}
        >
            <td colspan="8">
                <div class="flex flex-col items-center justify-center gap-4 w-full">
                    <span>Zur Zeit sind keine Dateien vorhanden</span>
                    <button class="btn btn-outline btn-accent p-2 min-h-0 h-fit" on:click={() => { goto('/upload'); }}>Datei hochladen</button>
                </div>
            </td>
        </tr>
        <tr id="rowID_{row.id}" class="row tablerow-normal">
            <td class="filename">{row.name}</td>
            <td class="tablecells-normal">{new Date(row.creationDate).toLocaleDateString("de-DE", dateOptions)}</td>
            <td class="tablecells-normal">{new Date(row.deletionDate).toLocaleDateString("de-DE", dateOptions)}</td>
            <td class="tablecells-mobile">
                <div class="flex flex-col justify-center">
                    {new Date(row.creationDate).toLocaleDateString("de-DE", dateOptions)}
                    {new Date(row.deletionDate).toLocaleDateString("de-DE", dateOptions)}
                </div>
            </td>
            <td>{row.currentDownloads} / {row.maxDownloads}</td>
            <td>
                <div class="tooltip tooltip-bottom" class:invisible={!row.encrypted} data-tip="Passwort geschützt">
                    <Icon src={TrOutlineLock} size="32" />
                </div>
            </td>
            <td></td>
            <td>
                <button class="btn btn-outline p-2 min-h-0 h-fit tooltip tooltip-bottom" data-tip="Download Link kopieren"
                        on:click={() => {getDownloadLink(row)}}
                >
                    <Icon src={AiOutlineLink} size="32" />
                </button>
            </td>
            <td>
                <button class="btn btn-outline btn-error p-2 min-h-0 h-fit tooltip tooltip-bottom tooltip-error"
                        data-tip="Datei löschen" on:click={() => {openConfirm(row.id)}}
                >
                    <Icon src={TrOutlineTrashX} size="32" />
                </button>
            </td>
        </tr>
        <tr id="rowID_{row.id}" class="row tablerow-mobile">
            <td colspan="7" class="filename text-left">{row.name}</td>
        </tr>
        <tr id="rowID_{row.id}_2" class="row tablerow-mobile">
            <td>
                <div class="flex flex-col justify-center">
                    {new Date(row.creationDate).toLocaleDateString("de-DE", dateOptions)}
                    {new Date(row.deletionDate).toLocaleDateString("de-DE", dateOptions)}
                </div>
            </td>
            <td colspan="2">{row.currentDownloads} / {row.maxDownloads}</td>
            <td>
                <div class="tooltip tooltip-bottom" class:invisible={!row.encrypted} data-tip="Passwort geschützt">
                    <Icon src={TrOutlineLock} size="32" />
                </div>
            </td>
            <td></td>
            <td>
                <button class="btn btn-outline p-2 min-h-0 h-fit tooltip tooltip-bottom" data-tip="Download Link kopieren"
                        on:click={() => {getDownloadLink(row)}}
                >
                    <Icon src={AiOutlineLink} size="32" />
                </button>
            </td>
            <td>
                <button class="btn btn-outline btn-error p-2 min-h-0 h-fit tooltip tooltip-bottom tooltip-error"
                        data-tip="Datei löschen" on:click={() => {openConfirm(row.id)}}
                >
                    <Icon src={TrOutlineTrashX} size="32" />
                </button>
            </td>
        </tr>
        <tr class="h-1 border-b tablerow-mobile"></tr>
    </FilterList>
    <div class="spacer-row h-28"></div>
</div>
<Modal bind:showModal={showConfirm} bind:dialog={confirmDialog} showClose={false}>
    <span slot="header" class="text-lg">Achtung!</span>
    <span>Wollen Sie die Datei wirklich löschen?</span>
    <div slot="buttons" class="flex flex-row gap-4">
        <button class="btn btn-outline btn-accent p-4 min-h-0 h-fit min-w-20" on:click={deleteFile}>Ja</button>
        <button class="btn btn-outline btn-error p-4 min-h-0 h-fit min-w-20" on:click={closeConfirm}>Nein</button>
    </div>
</Modal>
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

    .tablerow-normal {
        display: table-row;
    }
    .tablerow-mobile {
        @apply hidden;
    }
    .tablecells-normal {
        display: table-cell;
    }
    .tablecells-mobile {
        @apply hidden;
    }

    @media only screen and (max-width: 700px) {
        .header {
            @apply top-20;
        }

        .tablerow-normal {
            @apply hidden;
        }
        .tablerow-mobile {
            display: table-row;
        }
        .tablecells-normal {
            @apply hidden;
        }
        .tablecells-mobile {
            display: table-cell;
        }
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
    <div id="dev-tools" class="fixed right-4 top-20 flex flex-col gap-2 z-20">
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