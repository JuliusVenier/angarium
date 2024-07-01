<script>
    import { onMount } from "svelte";
    import { authenticateUser, isAuthenticated, returnToIndex, user} from "$lib/user.js";

    import User from "../../components/pages/+user.svelte"

    onMount(async () => {
        await authenticateUser();
        if (!$isAuthenticated) {
            returnToIndex();
        }
    });
</script>
<div class="flex justify-center overflow-y-auto h-full py-14">
    <User username={$user.username} />
</div>