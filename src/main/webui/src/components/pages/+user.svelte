<svelte:head>
    <title>angarium - Benutzerprofil</title>
</svelte:head>
<script>
    import { isAuthenticated, user, isDev, user_roles } from "$lib/user.js";
    import {pushPopup, popupColor} from "$lib/popup.js";

    export let username;
    let oldPassword;
    let changePassword;
    let newPassword;
    let newPasswordCheck;

    // checkFields ------------------------------------------------
    function checkFields() {
        return checkUsername() || checkNewPassword() ||checkNewPasswordCheck();
    }

    let usernameError = false;
    function checkUsername() {
        usernameError = username === undefined || username === null || username === "";
        return usernameError;
    }

    let newPasswordError = false;
    function checkNewPassword() {
        newPasswordError = newPassword === undefined || newPassword === null || newPassword === "";
    }

    let newPasswordCheckError = false;
    function checkNewPasswordCheck() {
        newPasswordCheckError = newPasswordCheck === undefined || newPasswordCheck === null || newPasswordCheck === "";
    }
    // ------------------------------------------------------------

    function changeUserData() {
        if (checkFields()) { return; }

        if (changePassword && newPassword !== newPasswordCheck) {
            console.error(new Error("password check failed"));
            pushPopup("Die neuen Passwörter stimmen nicht überein!", popupColor.error);
            return;
        }

        const oldLoginData = new URLSearchParams();
        oldLoginData.append("j_username", $user.username);
        oldLoginData.append("j_password", oldPassword);

        let password = newPassword;
        if (!changePassword) {
            password = oldPassword;
        }

        const updatedUser = {
            username: username,
            password: password
        };

        const newLoginData = new URLSearchParams();
        newLoginData.append("j_username", username);
        newLoginData.append("j_password", password);

        fetch("j_security_check", {
            method: "POST",
            body: oldLoginData,
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
        })
            .then(async (response) => {
                if (response.status === 200) {
                    console.log("authentication success");
                   await fetch("/api/user/me/update", {
                        method: "POST",
                        body: JSON.stringify(updatedUser),
                        headers: {"content-type": "application/json"}
                   })
                    .then(async (response) => {
                        console.log("/api/user/me/update", response.status, response.statusText);
                        if (response.status === 200) {
                            console.log("change success");
                            document.cookie = `quarkus-credential=; Max-Age=0;path=/`;

                            await fetch("j_security_check", {
                                method: "POST",
                                body: newLoginData,
                                headers: {
                                    "Content-Type": "application/x-www-form-urlencoded",
                                },
                            })
                                .then((response) => {
                                    if (response.status === 200) {
                                        pushPopup("Ihre Anmeldedaten wurden erfolgreich geändert.", popupColor.success, 2500);
                                    }
                                    else {
                                        throw new Error("New Login Failed!");
                                    }
                                })
                                .catch((ex) => {
                                    console.error(ex);
                                    document.cookie = `quarkus-credential=; Max-Age=0;path=/`;
                                });
                            window.location.reload();
                        }
                    })
                    .catch((ex) => {
                        console.error(ex);
                    });
                }
                else {
                    console.error(new Error("Wrong Password"));
                    pushPopup("Das eingegebene Passwort ist falsch!", popupColor.error);
                }
            })
            .catch((ex) => {
                console.error(ex);
            });
    }
</script>
<div class="flex flex-col w-1/5 min-w-80 gap-4 h-fit my-auto">
    {#if $user.role === user_roles.admin}
        <div class="flex-fit badge badge-accent badge-outline">admin</div>
    {/if}
    <div class="flex flex-row gap-4 items-center w-full">
        <span>Benutzername</span>
        <input type="text" class="input input-bordered w-full" bind:value={username}
               on:input={checkUsername} class:input-error={usernameError}/>
    </div>
    {#if username !== $user.username}
        <span class="text-warning">alter Benutzername: {$user.username}</span>
    {/if}
    <div></div>
    <input type="password" placeholder="altes Passwort" class="input input-bordered w-full" bind:value={oldPassword}/>
    <div class="flex flex-row gap-4 items-center w-full">
        <span>Passwort ändern</span>
        <input type="checkbox" class="checkbox checkbox-accent" bind:checked={changePassword}/>
    </div>
    {#if changePassword}
        <input type="password" placeholder="neues Passwort" class="input input-bordered w-full"
               bind:value={newPassword} on:input={checkNewPassword} class:input-error={newPasswordError}/>
        <input type="password" placeholder="neues Passwort wiederholen" class="input input-bordered w-full"
               bind:value={newPasswordCheck} on:input={checkNewPasswordCheck} class:input-error={newPasswordCheckError}/>
    {/if}
    <button class="btn btn-accent btn-outline" on:click={changeUserData}>Anmeldedaten ändern</button>
</div>
{#if $isDev}
    <div id="dev-tools" class="fixed right-4 top-20 flex flex-col gap-2">
    </div>
{/if}