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
    }

    .user-avatar {
        border-radius: 100px;
    }
    .user-avatar:hover{
        animation: user-avatar-animation .2s;
        border-radius: 10px;
    }
    @keyframes user-avatar-animation {
        from {border-radius: 100px;}
        to {border-radius: 10px;}
    }

    .login-window {
        position: fixed;
        width: 40vw;
        height: 50vh;

        top: calc(50vh - 25vh);
        left: calc(50vw - 20vw);

        border-radius: 20px;

        z-index: 100;
    }

    .navbar {
        height: 68px;
    }

</style>
<script>
    import "../app.css";
    import Login from '../components/+login.svelte';
    import { onMount } from 'svelte';

    let authenticated = false;
    onMount(() => {
        let cookie = document.cookie;
        if (cookie.includes("quarkus-credential")) {
            authenticated = true;
        }
    });

    let showLogin = false;
    function openLogin() {
        showLogin = true;
    }
    function closeLogin() {
        showLogin = false;
    }

    function logout() {
        document.cookie = `quarkus-credential=; Max-Age=0;path=/`;
        location.reload();
    }
</script>
<div class="page-layout">
    <div class="navbar bg-base-100">
        <div class="navbar-start">
            <a class="text-xl flex flex-row justify-center" href="../">
                <span class="font-bold bg-accent text-accent-content pb-1 pl-1 rounded">ang</span>
                <span class="pb-1">arium</span>
            </a>
        </div>
        <div class="navbar-center hidden lg:flex">
            <ul class="menu menu-horizontal px-1">
                <li><a href="../upload">Share</a></li>
                <!--<li>
                    <details>
                        <summary>Parent</summary>
                        <ul class="p-2">
                            <li><a>Submenu 1</a></li>
                            <li><a>Submenu 2</a></li>
                        </ul>
                    </details>
                </li>-->
            </ul>
        </div>
        <div class="navbar-end">
            {#if authenticated}
                <div id="login-button" class="size-12 bg-accent flex flex-col justify-center items-center rounded-full user-avatar" on:click={logout}>
                    <div class="size-4 rounded-full bg-accent-content"></div>
                    <div class="h-1"></div>
                    <div class="h-2 w-8 rounded-t-full bg-accent-content align-self-end"></div>
                </div>
            {:else}
                <div id="login-button" class="size-12 bg-accent flex flex-col justify-center items-center rounded-full user-avatar" on:click={openLogin}>
                    <div class="size-4 rounded-full bg-accent-content"></div>
                    <div class="h-1"></div>
                    <div class="h-2 w-8 rounded-t-full bg-accent-content align-self-end"></div>
                </div>
            {/if}

        </div>
    </div>
    <div class="divider-layout"></div>
    <div class="content-container">
        <slot />
    </div>
</div>
{#if showLogin}
    <div class="login-window bg-primary bg-opacity-50">
        <Login on:closeClick={closeLogin}/>
    </div>
    <div class="h-screen w-screen absolute top-0 left-0 z-10 bg-base-100 opacity-70"></div>
{/if}