<script>
    export let data;
    export let field;

    let search = '';

    $: regex = search ? new RegExp(search, 'i') : null;
    $: matches = (item) => regex ? regex.test(item[field]) : true;
</script>

<div class="flex flex-row gap-20 items-start justify-center">
    <div class="flex-none w-1/5 flex-row flex-1 gap-4 items-center sticky top-10">
        <input type="text" placeholder="Benutzername" class="input input-bordered w-full" bind:value={search}/>
    </div>
    <div class="w-1/2">
        <div class="border-b sticky top-0 pt-10 bg-white z-10">
            <slot name="header"/>
        </div>
        <div class="flex-1">
            {#each data.filter(matches) as item}
                <slot {item} />
            {/each}
        </div>
    </div>
</div>