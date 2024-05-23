<script>
    import { createEventDispatcher } from "svelte";

    const dispatch = createEventDispatcher();

    function closeClick() {
        dispatch('closeClick');
    }

    let loginSuccess = false;
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
                loginSuccess = true;
                setTimeout("5000");
                location.reload();
            } else {
                // Authentication failed
                console.error("Invalid credentials");
                loginSuccess = false;
                errorMessage = "Username oder Passwort sind falsch";
            }
        })
        .catch((error) => {
            loginSuccess = false;
            console.error(error);
            errorMessage = "Ein Fehler ist aufgetretten";
        });
    }

</script>
<div class="absolute w-4 h-4 top-4 right-4 cursor-pointer" on:click={closeClick}>
    <div class="w-4 h-0.5 bg-primary-content rotate-45 absolute top-2"></div>
    <div class="w-4 h-0.5 bg-primary-content -rotate-45 absolute top-2"></div>
</div>
<div class="flex flex-col h-full items-center justify-center">
    {#if loginSuccess}
        <div>Anmeldung erfolgreich</div>
    {:else}
        <div>Username</div>
        <input type="text" class="border border-base-300" bind:value={username}/>
        <div></div>
        <div>Passwort</div>
        <input type="password" class="border border-base-300" bind:value={password}/>
        <button type="button" on:click={login}>Login</button>
        {#if errorMessage !== null}
            <div>{errorMessage}</div>
        {/if}
    {/if}
</div>