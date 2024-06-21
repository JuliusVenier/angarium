<script>
    import { onMount } from "svelte";
    import { isAuthenticated, user, isDev } from "$lib/user.js";

    onMount(async () => {
        username = $user.username;
    });

    let username;
    let oldPassword;
    let changePassword;
    let newPassword;
    let checkNewPassword;
    function changeUserData() {
        if (newPassword !== checkNewPassword) {
            console.log("password check failed");
            return;
        }

        const loginData = new URLSearchParams();
        loginData.append("j_username", $user.username);
        loginData.append("j_password", oldPassword);

        fetch("j_security_check", {
            method: "POST",
            body: loginData,
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
        })
            .then(async (response) => {
                if (response.status === 200) {
                    let updatedUser = {
                        username: username,
                        password: newPassword
                    };

                    await fetch("/api/user/me/update", {
                        method: "POST",
                        body: JSON.stringify(updatedUser),
                        headers: {"content-type": "application/json"}
                    })
                        .then(async (response) => {
                            console.log("/api/user/me/update", response.status, response.statusText);
                            if (response.status === 204) {
                                console.log("success");
                                document.cookie = `quarkus-credential=; Max-Age=0;path=/`;

                                const newLoginData = new URLSearchParams();
                                newLoginData.append("j_username", username);
                                newLoginData.append("j_password", newPassword);
                                await fetch("j_security_check", {
                                    method: "POST",
                                    body: newLoginData,
                                    headers: {
                                        "Content-Type": "application/x-www-form-urlencoded",
                                    },
                                })
                                    .catch((ex) => {
                                        console.error(ex);
                                        document.cookie = `quarkus-credential=; Max-Age=0;path=/`;
                                    })
                                window.location.reload();
                            }
                        })
                        .catch((ex) => {
                            console.error(ex);
                        });
                }
            })
            .catch((ex) => {
                console.error(ex);
            });
        //Old Password is wrong
    }
</script>
<div class="flex flex-col h-full w-full items-center justify-center">
    <div class="flex flex-col w-1/5 min-w-80 gap-4">
        {#if $user.role === "admin"}
            <div class="flex-fit badge badge-accent badge-outline">admin</div>
        {/if}
        <div class="flex flex-row gap-4 items-center w-full">
            <span>Benutzername</span>
            <input type="text" class="input input-bordered w-full" bind:value={username}/>
        </div>
        {#if username !== $user.username}
            <span class="text-warning">alter Benutzername: {$user.username}</span>
        {/if}
        <div></div>
        <input type="password" placeholder="altes Passwort" class="input input-bordered w-full" bind:value={oldPassword}/>
        <div class="flex flex-row gap-4 items-center w-full">
            <span>Passwort ändern</span>
            <input type="checkbox" class="checkbox checkbox-accent" bind:checked={changePassword} />
        </div>
        {#if changePassword}
            <input type="password" placeholder="neues Passwort" class="input input-bordered w-full" bind:value={newPassword}/>
            <input type="password" placeholder="Passwort wiederholen" class="input input-bordered w-full" bind:value={checkNewPassword}/>
        {/if}
        <button class="btn btn-accent btn-outline" on:click={changeUserData}>Anmeldedaten ändern</button>
    </div>
</div>
{#if $isDev}
    <div id="dev-tools" class="fixed right-4 top-20 flex flex-col gap-2">
    </div>
{/if}