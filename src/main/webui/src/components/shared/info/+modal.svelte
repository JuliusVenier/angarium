<script>
    export let showModal;
    export let showClose = true;

    export let dialog;

    import {Icon} from 'svelte-icons-pack';
    import { CgClose } from "svelte-icons-pack/cg";

    $: if (dialog && showModal) dialog.showModal();

</script>
<dialog
        bind:this={dialog}
        on:close={() => {showModal = false}}
        on:click|self={() => dialog.close()}
>
    <div on:click|stopPropagation>
        <slot name="header" class="absolute top-2 left-2" />
        {#if showClose}
            <button class="absolute top-2 right-2" autofocus on:click={() => dialog.close()}><Icon src={CgClose} size="32" /></button>
        {/if}
        <div>
            <slot />
        </div>
        <slot name="buttons"/>
    </div>
</dialog>

<style>
    dialog {
        width: max(40vw, 400px);
        height: max(50vh, 200px);
        @apply rounded-lg;
        @apply border-2;
        padding: 0;
        background-image: repeating-linear-gradient(45deg, white 0px, white 20px, #f0f0f0 21px, white 22px);

    }
    dialog::backdrop {
        background: rgba(256, 256, 256, 0.7);
    }
    dialog > div {
        @apply p-10;
        @apply w-full;
        @apply h-full;
        @apply flex;
        @apply flex-col;
        @apply gap-4;
        @apply justify-center;
        @apply items-center;
    }
    dialog[open] {
        animation: zoom 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
    }
    @keyframes zoom {
        from {
            transform: scale(0.95);
        }
        to {
            transform: scale(1);
        }
    }
    dialog[open]::backdrop {
        animation: fade 0.2s ease-out;
    }
    @keyframes fade {
        from {
            opacity: 0;
        }
        to {
            opacity: 1;
        }
    }
    button {
        display: block;
    }
</style>
