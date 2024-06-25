<script>
    import {onMount} from "svelte";
    import {authenticateUser, returnToIndex, isAuthenticated, user} from "$lib/user.js";
    import Upload from "../../components/pages/+upload.svelte";

    onMount(async () => {
        await authenticateUser();
        if (!$isAuthenticated || $user.role !== "user") {
            returnToIndex();
        }
    });

    let scrollElement;
</script>
<div class="flex justify-center overflow-y-auto h-full py-14" bind:this={scrollElement}>
    <Upload scrollContainer={scrollElement} />
</div>