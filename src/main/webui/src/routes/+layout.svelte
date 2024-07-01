<style>
    .page-layout {
        display: flex;
        flex-flow: column nowrap;
    }

    .divider-layout {
        margin: 0;
        height: 1px;
        width: 100%;
        @apply bg-base-300;
    }

    .content-container {
        height: calc(100vh - 69px);
        @apply overflow-hidden;
    }

    .navbar {
        height: 68px;
    }

</style>
<script>
    import "../app.css";
    import { Icon } from 'svelte-icons-pack';
    import { CgMenuLeftAlt } from "svelte-icons-pack/cg";
    import angariumLogo from '$lib/angarium.svg';
    import Login from '../components/+login.svelte';
    import User_Avatar from '../components/+user_avatar.svelte';
    import {onMount} from 'svelte';
    import {authenticateUser, isAuthenticated, user, user_roles, logoutUser} from '$lib/user.js';
    import Modal from '../components/shared/info/+modal.svelte';
    import Popup from '../components/shared/info/+popup.svelte';

    onMount(async () => {
        await authenticateUser();
    });

    let isLoginOpen = false;
    function openLogin() {
        isLoginOpen = true;
    }
    function openLogout() {
        isLoginOpen = true;
        logoutUser();
    }
</script>
<div class="page-layout">
    <div class="navbar bg-base-100">
        <div class="navbar-start">
            <div class="dropdown dropdown-start dropdown-hover">
                <div tabindex="0" role="button" class="btn btn-ghost sm:hidden">
                    <Icon src={CgMenuLeftAlt} size="32"/>
                </div>
                <ul
                        tabindex="0"
                        class="menu dropdown-content bg-base-100 rounded-lg z-50 p-4 border-2"
                >
                    {#if $isAuthenticated && $user.role === user_roles.admin}
                        <li><a href="../download">Herunterladen</a></li>
                        <li><a href="../admin">Benutzer Verwaltung</a></li>
                    {:else if $isAuthenticated && $user.role === user_roles.user}
                        <li><a href="../upload">Hochladen</a></li>
                        <li><a href="../download">Herunterladen</a></li>
                        <li><a href="../my-files">Meine Dateien</a></li>
                    {:else}
                        <li><a href="../download">Herunterladen</a></li>
                    {/if}
                </ul>
            </div>
            <a class="text-xl flex flex-row justify-center" href="../">
                <img class="h-10" src="{angariumLogo}" alt="angarium"/>
            </a>
        </div>
        <div class="navbar-center hidden sm:flex">
            <ul class="menu menu-horizontal px-1">
                {#if $isAuthenticated && $user.role === user_roles.admin}
                    <li><a href="../download">Herunterladen</a></li>
                    <li><a href="../admin">Benutzer Verwaltung</a></li>
                {:else if $isAuthenticated && $user.role === user_roles.user}
                    <li><a href="../upload">Hochladen</a></li>
                    <li><a href="../download">Herunterladen</a></li>
                    <li><a href="../my-files">Meine Dateien</a></li>
                {:else}
                    <li><a href="../download">Herunterladen</a></li>
                {/if}
            </ul>
        </div>
        <div class="navbar-end">
            <User_Avatar on:openLogin={openLogin} on:logout={openLogout}/>
        </div>
    </div>
    <div class="divider-layout"></div>
    <div class="content-container">
            <slot />
    </div>
</div>
<Modal bind:showModal={isLoginOpen}>
    <Login isLoading={$isAuthenticated}/>
</Modal>
<Popup/>
