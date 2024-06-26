<script>
    import Download from "../../components/pages/+download.svelte";
    import {page} from '$app/stores';
    import {onMount} from "svelte";
    import {authenticateUser} from "$lib/user.js";

    onMount(async () => {
        await authenticateUser();
    });

    const paramID = $page.url.searchParams.get("id");
</script>
<div class="flex justify-center overflow-y-auto h-full py-14">
    <Download id={paramID}/>
</div>