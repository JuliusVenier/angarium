<script>
    import { onMount } from "svelte";

    let user = {
        id: null,
        username: null,
        role: null
    };
    onMount(() => {
        fetch("api/user/me/whoami", {
            method: "GET"
        })
            .then(async (response) => {
                console.log("api/user/me/whoami", response.status);
                user = await response.json();
            })
            .catch((ex) => {
                console.error(ex);
            });
    });

    let oldPassword;
    let newPassword;
    let checkNewPassword;
    function changePassword() {
        if (newPassword !== checkNewPassword) {
            console.log("password check failed");
            return;
        }

        const loginData = new URLSearchParams();
        loginData.append("j_username", user.username);
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
                        username: user.username,
                        password: newPassword
                    };

                    await fetch("/api/user/me/update", {
                        method: "POST",
                        body: JSON.stringify(updatedUser),
                        headers: {"content-type": "application/json"}
                    })
                        .then((response) => {
                            console.log("/api/user/me/update", response.status, response.statusText);
                            if (response.status === 201) {
                                console.log("success");
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
    <div class="flex flex-col w-1/5 gap-4">
        <div class="flex flex-row gap-4 items-center w-full">
            <span>Rolle</span>
            <span>{user.role ?? ""}</span>
        </div>
        <div class="flex flex-row gap-4 items-center w-full">
            <span>Benutzername</span>
            <input type="text" class="input input-bordered w-full" bind:value={user.username} disabled/>
        </div>
        <div></div>
        <div class="flex flex-row gap-4 items-center w-full">
            <span>Altes Passwort</span>
            <input type="password" class="input input-bordered w-full" bind:value={oldPassword}/>
        </div>
        <div class="flex flex-row gap-4 items-center w-full">
            <span>Neues Passwort</span>
            <input type="password" class="input input-bordered w-full" bind:value={newPassword}/>
        </div>
        <div class="flex flex-row gap-4 items-center w-full">
            <span>Passwort wiederholen</span>
            <input type="password" class="input input-bordered w-full" bind:value={checkNewPassword}/>
        </div>
        <button class="btn btn-accent btn-outline" on:click={changePassword}>Passwort ändern</button>
    </div>
</div>
{#if true}
    <div id="dev-tools" class="fixed right-4 top-20 flex flex-col gap-2">
    </div>
{/if}