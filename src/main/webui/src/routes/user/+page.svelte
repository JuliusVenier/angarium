<script>
    import { onMount } from "svelte";
    import { authenticateUser, isAuthenticated, returnToIndex} from "$lib/user.js";

    import User from "../../components/pages/+user.svelte"

    onMount(async () => {
        await authenticateUser();
        if (!$isAuthenticated) {
            returnToIndex();
        }
    });
</script>
<User />