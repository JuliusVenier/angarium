<script>
    export let staticTable = false;
    export let data;
    export let field;

    let search = '';

    $: regex = search ? new RegExp(search, 'i') : null;
    $: matches = (item) => regex ? regex.test(item[field]) : true;
</script>
<div class="filterList-container">
    <div class="flex-none w-1/5 flex-row flex-1 gap-4 items-center sticky top-10">
        <input type="text" placeholder="Benutzername" class="input input-bordered w-full min-w-80" bind:value={search}/>
    </div>
    {#if staticTable}
        <table class="w-1/2 min-w-80">
            <slot name="colgroup"/>
            <slot name="header"/>
            <slot name="empty" />
            {#each data.filter(matches) as item}
                <slot {item} />
            {/each}
        </table>
    {:else}
        <div class="w-1/2 min-w-80">
            <div class="border-b sticky top-0 pt-10 bg-white z-10">
                <slot name="header"/>
            </div>
            <div class="flex-1">
                {#each data.filter(matches) as item}
                    <slot {item} />
                {/each}
            </div>
        </div>
    {/if}
</div>
<style>
    .filterList-container {
        @apply flex;
        @apply flex-row;
        @apply gap-20;
        @apply items-start;
        @apply justify-center;
    }

    @media only screen and (max-width: 600px) {
        .filterList-container {
            @apply flex;
            @apply flex-col;
            @apply gap-20;
            @apply items-center;
            @apply justify-start;
        }
    }
</style>