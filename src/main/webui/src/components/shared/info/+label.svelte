<script>
    import { Icon } from 'svelte-icons-pack';

    export let icon;
    export let text;
    export let fg_color;
    export let bg_color
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
</style>

<div class="label">
    {#if icon !== null && icon !== undefined}
        <Icon src={icon} size="32"/>
    {/if}
    <span>{text}</span>
</div>