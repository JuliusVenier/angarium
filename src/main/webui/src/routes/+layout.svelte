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

    .login-window {
        position: fixed;
        width: 40vw;
        height: 50vh;

        top: calc(50vh - 25vh);
        left: calc(50vw - 20vw);

        border-radius: 20px;

        z-index: 100;

        background-image: repeating-linear-gradient(45deg, white 0px, white 20px, #f0f0f0 21px, white 22px);
    }

    .navbar {
        height: 68px;
    }

</style>
<script>
    import "../app.css";
    import Login from '../components/+login.svelte';
    import User_Avatar from '../components/+user_avatar.svelte';
    import {onMount} from 'svelte';
    
    let authenticated = false;
    onMount(() => {
        let cookie = document.cookie;
        if (cookie.includes("quarkus-credential")) {
            authenticated = true;
        }
    });

    let openedLogout = false;
    let showLogin = false;
    function openLogin() {
        showLogin = true;
    }
    function closeLogin() {
        showLogin = false;
    }

    function logout() {
        document.cookie = `quarkus-credential=; Max-Age=0;path=/`;
        openedLogout = true;
        showLogin = true;
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
                <li><a href="../upload">Upload</a></li>
                <li><a href="../download">Download</a></li>
                <li><a href="../admin">Benutzer Verwaltung</a></li>
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
            <User_Avatar authenticated={authenticated} on:openLogin={openLogin} on:logout={logout}/>
        </div>
    </div>
    <div class="divider-layout"></div>
    <div class="content-container">
        <slot />
    </div>
</div>
{#if showLogin}
    <div class="login-window border-2">
        <Login isLoading={openedLogout} on:closeClick={closeLogin}/>
    </div>
    <div class="h-screen w-screen absolute top-0 left-0 z-10 bg-base-100 opacity-70"></div>
{/if}