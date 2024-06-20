<script>
    export let text;
    export let color;
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
        @apply text-white;
        @apply bg-success;
    }
    .label-warning {
        @apply text-white;
        @apply bg-warning;
    }
    .label-error{
        @apply text-white;
        @apply bg-error;
    }
</style>
<div class="label label-{color}">
    <slot name="icon"/>
    <span>{text}</span>
</div>