<script>
    export let icon;
    export let bgColor = true;

    import {onMount} from "svelte";

    let bgClass;
    onMount(() => {
        if (!bgColor) {
            bgClass = icon + "-content";
            return;
        }
        bgClass = icon;
    });

</script>
<style>
    .icon-bg-success {
        @apply bg-success;
    }
    .icon-bg-success-content {
        @apply bg-success-content;
    }
    .icon-bg-warning {
        @apply bg-warning;
    }
    .icon-bg-warning-content {
        @apply bg-warning-content;
    }
    .icon-bg-error {
        @apply bg-error;
    }
    .icon-bg-error-content {
        @apply bg-error-content;
    }
</style>
{#if icon === "success"}
    <div class="w-max h-max">
        <div class="w-3 h-1.5 rounded-sm icon-bg-{bgClass} rotate-45"></div>
        <div class="w-6 h-1.5 -mt-2 ml-1 rounded-sm icon-bg-{bgClass} -rotate-45"></div>
    </div>
{:else if icon === "warning" || icon === "info"}
    <div class="w-max h-max">
        <div class="w-2 h-4 rounded-sm icon-bg-{bgClass}"></div>
        <div class="w-2 h-2 mt-0.5 rounded-sm icon-bg-{bgClass}"></div>
    </div>
{:else if icon === "error"}
    <div class="flex flex-row w-max h-max">
        <div class="w-6 h-1.5 rounded-sm icon-bg-{bgClass} rotate-45"></div>
        <div class="w-6 h-1.5 -ml-6 rounded-sm icon-bg-{bgClass} -rotate-45"></div>
    </div>
{/if}