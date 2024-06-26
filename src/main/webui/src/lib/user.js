import {readable, writable} from "svelte/store";

export const user_roles = {
    null: null,
    admin: "admin",
    user: "user"
};

export const isDev = readable(false);
export const isAuthenticated = writable(false);
export const user = writable({
    id: null,
    username: "",
    role: user_roles.null
});

export async function authenticateUser() {
    isAuthenticated.set(false);
    user.set({
        id: null,
        username: "",
        role: user_roles.null
    });
    await fetch("api/user/me/whoami", {
        method: "GET"
    })
        .then(async (response) => {
            if (response.status === 200) {
                isAuthenticated.set(true);
                user.set(await response.json());
            }
        });
}

export function logoutUser() {
    document.cookie = `quarkus-credential=; Max-Age=0;path=/`;
    reloadSite(1000);
}

export function returnToIndex() {
    let link = document.createElement("a");
    link.href = ".";
    link.click();
}

export function reloadSite(delay) {
    setTimeout(() => {
        returnToIndex();
    }, delay);
}