<style>
    .user-avatar {
        @apply rounded-full;
    }
    .user-avatar:hover {
        animation: user-avatar-animation .2s;
        @apply rounded-t-lg;
        @apply rounded-b-none;
    }
    @keyframes user-avatar-animation {
        from {border-radius: 100px;}
        to {border-radius: 10px;}
    }
    .user-avatar-dropdown:hover .user-avatar {
        @apply rounded-t-lg;
        @apply rounded-b-none;
    }
</style>
<script>
    export let authenticated;

    import {createEventDispatcher} from "svelte";
    const dispatch = createEventDispatcher();

    function openLogin() {
        dispatch("openLogin");
    }

    function logout() {
        dispatch("logout");
    }
</script>
{#if !authenticated}
    <div class="dropdown dropdown-end dropdown-hover">
        <div id="login-button" class="size-12 bg-accent-content border-2 border-accent flex flex-col justify-center items-center rounded-full user-avatar"
             tabindex="0" role="button"
        >
            <div class="size-4 rounded-full bg-accent"></div>
            <div class="h-1"></div>
            <div class="h-2 w-8 rounded-t-full bg-accent align-self-end"></div>
        </div>
        <ul tabindex="0" class="dropdown-content z-[1] menu p-2 bg-base-100 rounded-lg rounded-tr-none border-2 border-accent user-avatar-dropdown">
            <li><a on:click={openLogin}>Anmelden</a></li>
        </ul>
    </div>
{:else}
    <div class="dropdown dropdown-end dropdown-hover">
        <div id="login-button" class="size-12 bg-accent flex flex-col justify-center items-center rounded-full user-avatar"
             tabindex="0" role="button"
        >
            <div class="size-4 rounded-full bg-accent-content"></div>
            <div class="h-1"></div>
            <div class="h-2 w-8 rounded-t-full bg-accent-content align-self-end"></div>
        </div>
        <ul tabindex="0" class="dropdown-content z-[1] menu p-2 bg-base-100 rounded-lg rounded-tr-none border-2 border-accent user-avatar-dropdown">
            <li><a href="/user">Profil</a></li>
            <li><a on:click={logout}>Abmelden</a></li>
        </ul>
    </div>
{/if}