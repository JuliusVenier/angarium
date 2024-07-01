<script>
    import {returnToIndex, reloadSite} from "$lib/user.js";
    import {createEventDispatcher, onMount} from "svelte";

    // Icon imports ---------------------------------------
    import {Icon} from 'svelte-icons-pack';
    import {SlClose} from "svelte-icons-pack/sl";
    // ----------------------------------------------------

    let window;

    onMount(() => {
       window.addEventListener('keypress', (e) => {
           if (e.key === 'Enter') {
               login();
           }
       })
    });

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
                isLoading = true;
                console.log("Authentication successful");
                loadingMessage = "Anmeldung erfolgreich";
                reloadSite();
            } else {
                // Authentication failed
                isLoading = false;
                console.error("Invalid credentials");
                errorMessage = "Username oder Passwort ist falsch";

            }
        })
        .catch((error) => {
            isLoading = false;
            console.error(error);
            errorMessage = "Ein Fehler ist aufgetreten";
        });
    }

    function inputChanged() {
        errorMessage = null;
    }
</script>
<div class="flex flex-col w-full h-full" bind:this={window}>
    <div class="flex flex-col h-full items-center justify-center gap-4">
        {#if isLoading}
            <span class="loading loading-spinner text-accent"></span>
            <div>{loadingMessage}</div>
        {:else}
            <input type="text" placeholder="Benutzername" class="input input-bordered w-full max-w-xs min-w-60" bind:value={username} on:input={inputChanged}/>
            <input type="password" placeholder="Passwort" class="input input-bordered w-full max-w-xs min-w-60" bind:value={password} on:input={inputChanged}/>
            <button class="btn btn-outline btn-accent" on:click={login}>Einloggen</button>
            {#if errorMessage !== null}
                <div class="alert alert-error w-fit">
                    <Icon src={SlClose} size="32" slot="icon"/>
                    <span>{errorMessage}</span>
                </div>
            {/if}
        {/if}
    </div>
</div>