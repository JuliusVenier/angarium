<script>
    import {isDev, user} from "$lib/user.js";
    import {onMount} from "svelte";
    import FilterList from '../shared/+filterList.svelte';
    import Modal from '../shared/info/+modal.svelte'

    // Icon imports ---------------------------------------
    import {Icon} from 'svelte-icons-pack';
    import {TrOutlineTrashX} from "svelte-icons-pack/tr";
    import {SlReload} from "svelte-icons-pack/sl";
    import {AiOutlineUserAdd} from "svelte-icons-pack/ai";
    import {CgClose} from "svelte-icons-pack/cg";
    import {RiDeviceSave3Line} from "svelte-icons-pack/ri";
    //-----------------------------------------------------

    let newUserItem_exists = false;
    let newUser_name;

    let showConfirm = false;

    let scrollElement;
    let userList = [];
    onMount(async () => {
        fetchUserList();
    });

    function fetchUserList() {
        fetch("api/user/all", {
            method: "GET"
        })
            .then(async (response) => {
                console.log("api/user/all", response.status, response.statusText);
                if (response.status === 200) {
                    userList = await response.json();
                }
            })
            .catch((ex) => {
                console.error(ex);
            });
    }

    let newRowInput;
    async function addUser() {
        if (newUserItem_exists) {
            return;
        }

        let newUser = {
            id: "new",
            username: "",
            role: "user"
        };
        userList = [...userList, newUser];
        newUserItem_exists = true;

        await scrollElement.scroll({top: scrollElement.scrollHeight, behavior: 'smooth'});
        newRowInput.focus();
    }

    function abortNewUser() {
        if (!newUserItem_exists) { return; }

        userList.pop();
        userList = userList;

        newUserItem_exists = false;
    }

    function saveNewUser() {
        let body = {
            username: newUser_name
        };

        fetch("api/user", {
            method: "POST",
            body: JSON.stringify(body),
            headers: {'content-type': 'application/json'}
        })
            .then(async (response) => {
                console.log("api/user (POST)", response.status);
                if (response.status === 200) {
                    let newUser = await response.json();

                    userList.pop();
                    userList = [...userList, newUser];
                    newUserItem_exists = false;
                    newUser_name = undefined;
                } else {
                    throw new Error("Unexpected Response: " + response.status + " (" + response.statusText + ")");
                }
            })
            .catch((error) => {
                console.error(error);
            });
    }

    function resetPassword(userID) {
        fetch("api/user/reset/" + userID, {
            method: "POST"
        })
            .then((response) => {
                console.log("api/user/reset/" + userID + " (POST)", response.status);
                if (response.status === 204) {
                    return null;
                }
                else {
                    throw new Error("Unexpected Response: " + response.status + " (" + response.statusText + ")");
                }
            })
            .catch((error) => {
                console.error(error);
            });
        return null;
    }

    function deleteUser(userID) {
        fetch("api/user/" + userID, {
            method: "DELETE"
        })
            .then((response) => {
                console.log("api/user/" + userID + " (DELETE)", response.status);
                if (response.status === 204) {
                    window.location.reload();
                }
                else {
                    throw new Error("Unexpected Response: " + response.status + " (" + response.statusText + ")");
                }
            })
            .catch((error) => {
                console.error(error);
            });
        return null;
    }
</script>
<div bind:this={scrollElement} class="h-full w-full overflow-y-auto">
    <FilterList
            data={userList}
            field="username"
            let:item={row}
    >
        <header class="header" slot="header">
            <span class="id">ID</span>
            <span class="username">Benutzername</span>
            <span class="flex-1"></span>
            <button class="btn btn-outline btn-success p-2 min-h-0 h-fit tooltip tooltip-top tooltip-success" data-tip="Benutzer anlegen" on:click={addUser}><Icon src={AiOutlineUserAdd} size="32"/></button>
        </header>
        <div id="rowID_{row.id}" class="row">
            {#if row.id === "new"}
                <span class="userID">+</span>
                <input bind:this={newRowInput} type="text" placeholder="Benutzername" class="input input-bordered w-full" bind:value={newUser_name}/>
                <div class="flex-1"></div>
                <button class="btn btn-outline btn-accent p-2 min-h-0 h-fit tooltip tooltip-bottom tooltip-accent" data-tip="Neuen Benutzer speichern" on:click={saveNewUser}><Icon src={RiDeviceSave3Line} size="32" /></button>
                <button class="btn btn-outline btn-error p-2 min-h-0 h-fit tooltip tooltip-bottom tooltip-error" data-tip="Abbrechen" on:click={abortNewUser}><Icon src={CgClose} size="32" /></button>
            {:else}
                <span class="userID">{row.id}</span>
                <span class="username">{row.username}</span>
                {#if row.role === "admin"}
                    <div class="flex-fit badge badge-accent badge-outline">admin</div>
                {/if}
                <div class="flex-1"></div>
                <button class="btn btn-outline p-2 min-h-0 h-fit tooltip tooltip-bottom" data-tip="Passwort zurücksetzen" class:btn-disabled={newUserItem_exists} on:click={resetPassword(row.id)}><Icon src={SlReload} size="32" /></button>
                <button class="btn btn-outline btn-error p-2 min-h-0 h-fit tooltip tooltip-bottom tooltip-error" data-tip="Benutzer löschen" class:btn-disabled={newUserItem_exists || row.role === "admin"} on:click={() => {/*showConfirm = true;*/ deleteUser(row.id);}}><Icon src={TrOutlineTrashX} size="32" /></button>
            {/if}
        </div>
    </FilterList>
    <div class="spacer-row h-28"></div>
</div>
<Modal bind:showConfirm>
    <span slot="header">Achtung!</span>
    <span>Wollen Sie den Benutzer wirklich löschen?</span>
    <div class="flex flex-row gap-4">
        <button class="btn btn-outline p-2 min-h-0 h-fit">Ja</button>
        <button class="btn btn-outline p-2 min-h-0 h-fit">Nein</button>
    </div>
</Modal>
<style>
    .header {
        @apply flex;
        @apply flex-row;
        @apply items-center;
        @apply h-14;
        @apply gap-4;
        @apply px-4;
    }

    .row {
        @apply flex;
        @apply flex-row;
        @apply items-center;
        @apply h-14;
        @apply gap-4;
        @apply px-4;
        background: var(--bg-1);
        @apply rounded-md;
        @apply border-2;
        @apply border-solid;
        @apply border-white;
    }

    .row:hover:not(header) {
        @apply border-accent;
    }

</style>
{#if $isDev}
    <div id="dev-tools" class="fixed right-4 top-20 flex flex-col gap-2">
        <div class="flex flex-row flex-wrap items-center justify-end gap-2">
            <button class="btn w-20" on:click={fetchUserList}>fetchRows</button>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end gap-2">
            <button class="btn w-20" on:click={() => { userList = [...userList, userList.at(-1)]; }}>addRow</button>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end gap-2">
            <button class="btn w-20" on:click={() => { userList.pop(); userList = userList; }}>popRow</button>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-end gap-2">
            <button class="btn w-20" on:click={() => { userList = userList.concat(userList); }}>dupeRows</button>
        </div>
    </div>
{/if}