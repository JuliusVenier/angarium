<script>
    import Icon from "./+icon.svelte";

    export let type;
    export let text;
    export let showIcon = false;
</script>
<style>

    .label {
        @apply alert;
        @apply h-12;
        @apply w-max;
        @apply flex;
        @apply flex-row;
        @apply justify-center;

    }

    .label-success {
        @apply alert-success;
    }

    .label-warning {
        @apply alert-warning;
    }

    .label-error {
        @apply alert-error;
    }

</style>

<div class="label label-{type}">
    {#if showIcon}
        <Icon icon={type} bgColor={false}/>
    {/if}
    <span>{text}</span>
</div>