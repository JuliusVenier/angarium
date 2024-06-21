<script>
    import Download from "../../components/pages/+download.svelte";
    import { page } from '$app/stores';

    const paramID = $page.url.searchParams.get("id");
</script>
<Download id={paramID}/>