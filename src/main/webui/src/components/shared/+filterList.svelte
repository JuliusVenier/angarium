<script>
    export let staticTable = false;
    export let data;
    export let field;

    export let searchPlaceholder = "Suche";

    let search = '';

    $: regex = search ? new RegExp(search, 'i') : null;
    $: matches = (item) => regex ? regex.test(item[field]) : true;
</script>
<div class="filterList-container">
    <div class="search-bar">
        <input type="text" placeholder={searchPlaceholder} class="input input-bordered w-full" bind:value={search}/>
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
            <div class="header-sticky">
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
    .search-bar {
        @apply w-1/5;
        @apply min-w-32;
        @apply sticky;
        @apply top-10;
    }

    .header-sticky {
        @apply border-b;
        @apply sticky;
        @apply top-0;
        @apply pt-10;
        @apply bg-white;
        @apply z-10;
    }

    .filterList-container {
        @apply flex;
        @apply flex-row;
        @apply gap-20;
        @apply items-start;
        @apply justify-center;
    }

    @media only screen and (max-width: 700px) {
        .filterList-container {
            @apply flex;
            @apply flex-col;
            @apply gap-0;
            @apply items-center;
            @apply justify-center;
        }

        .search-bar {
            @apply flex;
            @apply flex-col;
            @apply items-center;
            @apply justify-center;
            @apply sticky;
            @apply top-0;
            @apply w-full;
            @apply pt-8;
            @apply bg-white;
            @apply z-10;
        }

        .search-bar > input {
            @apply w-80;
        }

        .header-sticky {
            @apply border-b;
            @apply sticky;
            @apply top-20;
            @apply pt-10;
            @apply bg-white;
            @apply z-10;
        }
    }
</style>