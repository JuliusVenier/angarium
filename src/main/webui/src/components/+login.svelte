<script>
    import {createEventDispatcher} from "svelte";
    import { onMount } from "svelte";

    let window;
    const dispatch = createEventDispatcher();

    onMount(() => {
        if (isLoading) { reloadSite(); }
       window.addEventListener('keypress', (e) => {
           if (e.key === 'Enter') {
               login();
           }
       })
    });

    function closeClick() {
        dispatch('closeClick');
    }

    export let isLoading = false;
    let loadingMessage = "Abmeldung erfolgreich";
    let errorMessage = null;
    let username;
    let password;

    function login() {
        const loginData = new URLSearchParams();
        loginData.append("j_username", username);
        loginData.append("j_password", password);

        fetch("j_security_check", {
            method: "POST",
            body: loginData,
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
        })
        .then((response) => {
            if (response.status === 200) {
                // Authentication was successful
                console.log("Authentication successful");
                loadingMessage = "Anmeldung erfolgreich";
                isLoading = true;
                reloadSite();
            } else {
                // Authentication failed
                console.error("Invalid credentials");
                isLoading = false;
                errorMessage = "Username oder Passwort ist falsch";
            }
        })
        .catch((error) => {
            isLoading = false;
            console.error(error);
            errorMessage = "Ein Fehler ist aufgetretten";
        });
    }

    function inputChanged() {
        errorMessage = null;
    }

    function reloadSite() {
        setTimeout(() => { location.reload(); }, 1000);
    }

</script>
<div class="flex flex-col w-full h-full" bind:this={window}>
    <div class="w-8 h-8 cursor-pointer self-end flex flex-row justify-center items-center">
        <div class="w-4 h-4 cursor-pointer absolute top-4" on:click={closeClick}>
            <div class="w-4 h-0.5 bg-base-content rotate-45 absolute"></div>
            <div class="w-4 h-0.5 bg-base-content -rotate-45 absolute"></div>
        </div>
    </div>
    <div class="flex flex-col h-full items-center justify-center gap-4">
        {#if isLoading}
            <span class="loading loading-spinner text-accent"></span>
            <div>{loadingMessage}</div>
        {:else}
            <input type="text" placeholder="Benutzername" class="input input-bordered w-full max-w-xs" bind:value={username} on:input={inputChanged}/>
            <input type="password" placeholder="Passwort" class="input input-bordered w-full max-w-xs" bind:value={password} on:input={inputChanged}/>
            <button class="btn btn-outline btn-accent" on:click={login}>Einloggen</button>
            {#if errorMessage !== null}
                <div class="alert alert-error w-1/2">
                    <span>{errorMessage}</span>
                </div>
            {/if}
        {/if}
    </div>
</div>