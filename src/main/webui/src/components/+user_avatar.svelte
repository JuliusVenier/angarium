<style>
    .user-avatar {
        @apply rounded-full;
    }
    .user-avatar-animation {
        animation: user-avatar-animation_out 1s;
    }
    .user-avatar:hover {
        animation: user-avatar-animation_in .2s;
        @apply rounded-lg;
    }
    @keyframes user-avatar-animation_in {
        from {border-radius: 100px;}
        to {border-radius: 10px;}
    }
    @keyframes user-avatar-animation_out {
        from {border-radius: 10px;}
        to {border-radius: 100px;}
    }
</style>
<script>
    import { onMount } from "svelte";
    import { isAuthenticated, user } from "$lib/user.js";
    import {createEventDispatcher} from "svelte";
    const dispatch = createEventDispatcher();

    let avatar;
    onMount(() => {
        const avatar_hover_event = (event) => {
            console.log("addclass");
            avatar.classList.add('user-avatar-animation');
            avatar.removeEventListener("hover");
        }
        avatar.addEventListener('hover', avatar_hover_event);
    });

    function openLogin() {
        dispatch("openLogin");
    }

    function logout() {
        dispatch("logout");
    }

</script>
{#if $isAuthenticated === false}
    <div class="dropdown dropdown-end dropdown-hover">
        <div id="login-button" class="size-12 bg-accent-content border-2 border-accent flex flex-col justify-center items-center rounded-full user-avatar"
             tabindex="0" role="button" bind:this={avatar}
        >
            <div class="size-4 rounded-full bg-accent"></div>
            <div class="h-1"></div>
            <div class="h-2 w-8 rounded-t-full bg-accent align-self-end"></div>
        </div>
        <ul tabindex="0" class="dropdown-content z-50 menu p-4 bg-base-100 rounded-lg border-2 user-avatar-dropdown">
            <li><a on:click={openLogin}>Anmelden</a></li>
        </ul>
    </div>
{:else}
    <div class="dropdown dropdown-end dropdown-hover">
        <div id="login-button" class="size-12 bg-accent flex flex-col justify-center items-center rounded-full user-avatar"
             tabindex="0" role="button" bind:this={avatar}
        >
            <div class="size-4 rounded-full bg-accent-content"></div>
            <div class="h-1"></div>
            <div class="h-2 w-8 rounded-t-full bg-accent-content align-self-end"></div>
        </div>
        <ul tabindex="0" class="dropdown-content z-50 menu p-4 bg-base-100 rounded-lg border-2 user-avatar-dropdown">
            <li><a href="/user">{$user.username}</a></li>
            <li><a on:click={logout}>Abmelden</a></li>
        </ul>
    </div>
{/if}