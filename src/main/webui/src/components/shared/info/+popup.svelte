<script>
    import {isDev} from "$lib/user.js";
    import {popupArray, pushPopup, popPopup, popupColor} from "$lib/popup.js";

    import {Icon} from 'svelte-icons-pack';
    import {SlCheck, SlClose} from "svelte-icons-pack/sl";
    import {RiSystemErrorWarningLine} from "svelte-icons-pack/ri";
    import { CgClose } from "svelte-icons-pack/cg";
</script>
<div class="flex flex-col gap-6 items-end absolute bottom-0 right-0 p-4">
    {#each $popupArray as popup}
        <div class="popup"
             class:bg-accent={popup.color === popupColor.accent}
             class:bg-success={popup.color === popupColor.success}
             class:bg-warning={popup.color === popupColor.warning}
             class:bg-error={popup.color === popupColor.error}
        >
            {#if popup.color === popupColor.success}
                <Icon src={SlCheck} size="32" />
            {:else if popup.color === popupColor.warning}
                <Icon src={RiSystemErrorWarningLine} size="32" />
            {:else if popup.color === popupColor.error}
                <Icon src={SlClose} size="32" />
            {/if}
            <span>{popup.text}</span>
            <button class="self-start" on:click={() => {popup.removeSelf()}}><Icon src={CgClose} size="16" /></button>
        </div>
    {/each}
</div>
<style>
    .popup {
        @apply rounded-lg;
        @apply py-1;
        @apply pr-1;
        @apply pl-4;
        @apply w-fit;
        @apply h-16;
        @apply flex;
        @apply flex-row;
        @apply gap-4;
        @apply justify-center;
        @apply items-center;
        @apply z-50;
        @apply text-white;

        animation: slide-from-bottom 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
    }

    @keyframes slide-from-bottom {
        from {
            bottom: -200px;
        }
        to {
            @apply bottom-4;
        }
    }
</style>
{#if $isDev}
    <div id="dev-tools" class="fixed left-4 bottom-4 flex flex-col gap-2">
        <div class="flex flex-row flex-wrap items-center justify-start gap-2">
            <button class="btn w-fit" on:click={() => { pushPopup("Test Accent", popupColor.accent) }}>addAccentPopup</button>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-start gap-2">
            <button class="btn w-fit" on:click={() => { pushPopup("Test Success", popupColor.success) }}>addSuccessPopup</button>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-start gap-2">
            <button class="btn w-fit" on:click={() => { pushPopup("Test Warning", popupColor.warning) }}>addWarningPopup</button>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-start gap-2">
            <button class="btn w-fit" on:click={() => { pushPopup("Test Error", popupColor.error) }}>addErrorPopup</button>
        </div>
        <div class="flex flex-row flex-wrap items-center justify-start gap-2">
            <button class="btn w-fit" on:click={() => { popPopup() }}>popPopup</button>
        </div>
    </div>
{/if}