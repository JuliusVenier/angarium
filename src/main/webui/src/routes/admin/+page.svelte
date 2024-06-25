<script>
    import {onMount} from "svelte";
    import {authenticateUser, isAuthenticated, returnToIndex, user, user_roles} from "$lib/user.js";
    import Admin from "../../components/pages/+admin.svelte";

    onMount(async () => {
        await authenticateUser();
        if (!$isAuthenticated || $user.role !== user_roles.admin) {
            console.log("redirect");
            returnToIndex();
        }
    });
</script>
<Admin />