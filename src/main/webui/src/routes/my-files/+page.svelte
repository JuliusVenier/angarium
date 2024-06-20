<script>
    import { onMount } from "svelte";
    import {authenticateUser, returnToIndex, user_roles, isAuthenticated, user} from "$lib/user.js";
    import My_Files from "../../components/pages/+my-files.svelte";

    onMount(async () => {
        await authenticateUser();
        if (!$isAuthenticated || $user.role !== user_roles.user) {
            returnToIndex();
        }
    });
</script>
<My_Files />