<script>
    import { onMount } from "svelte";
    import { authenticateUser, isAuthenticated, user, user_roles } from "$lib/user.js";

    import My_Files from "../components/pages/+my-files.svelte";
    import Admin from "../components/pages/+admin.svelte";
    import Download from "../components/pages/+download.svelte";

    const pages = {
        my_files: 0,
        admin: 1,
        download: 2
    }
    let page;
    onMount(async () => {
        await authenticateUser();
        if ($isAuthenticated && $user.role === user_roles.user) {
            page = pages.my_files
        }
        else if ($isAuthenticated && $user.role === user_roles.admin) {
            page = pages.admin;
        }
        else {
            page = pages.download;
        }
    });
</script>
{#if page === pages.my_files}
    <My_Files/>
{:else if page === pages.admin}
    <Admin/>
{:else if page === pages.download}
    <div class="flex justify-center overflow-y-auto h-full py-14">
        <Download id={null}/>
    </div>
{/if}