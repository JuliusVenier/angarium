<script>
    import {onMount} from "svelte";
    import {authenticateUser, isAuthenticated, returnToIndex, user, user_roles} from "$lib/user.js";
    import My_Files from "../../components/pages/+my-files.svelte";

    onMount(async () => {
        await authenticateUser();
        if (!$isAuthenticated || $user.role !== user_roles.user) {
            returnToIndex();
        }
    });
</script>
<My_Files />