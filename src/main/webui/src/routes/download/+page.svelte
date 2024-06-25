<script>
    import Download from "../../components/pages/+download.svelte";
    import { page } from '$app/stores';

    const paramID = $page.url.searchParams.get("id");
</script>
<div class="flex justify-center overflow-y-auto h-full py-14">
    <Download id={paramID}/>
</div>