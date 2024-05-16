<style>

    .scroll-container {
        overflow-y: auto;
        @apply snap-y;
    }

    .section-header {
        @apply mb-10;
        @apply font-bold;
        @apply text-xl;
    }

    .section-content {
        @apply ml-8;
    }

    .divider {
        @apply mb-20;
    }

</style>
<script>
    import { onMount } from "svelte";

    const fileID = "file";
    const nameID = "name";
    const securityID = "sec";
    const sharingID = "sha";
    const fileTxt = "Datei auswählen";
    const nameTxt = "Name vergeben";
    const securityTxt = "Schutz auswählen";
    const sharingTxt = "Sharing Methode";

    let scrollContainer;
    let scrollPositionItem1;
    let scrollPositionItem2;
    let scrollPositionItem3;
    let scrollPositionItem4;
    let progress = 1;

    onMount(()=> {
        scrollContainer.addEventListener('scroll', function(e) {
            handleScroll();
        });
    });

    function handleScroll() {
        let scrollpos1 = scrollPositionItem1.getBoundingClientRect().top;
        let scrollpos2 = scrollPositionItem2.getBoundingClientRect().top;
        let scrollpos3 = scrollPositionItem3.getBoundingClientRect().top;
        let scrollpos4 = scrollPositionItem4.getBoundingClientRect().top;

        console.log(scrollpos2);
        let min = 69 - 10;
        let max = 69 + 10;

        if (scrollpos4 >= min && scrollpos4 <= max) {
            progress = 4;
        }
        else if (scrollpos3 >= min && scrollpos3 <= max) {
            progress = 3;
        }
        else if (scrollpos2 >= min && scrollpos2 <= max) {
            progress = 2;
        }
        else if (scrollpos1 >= min && scrollpos1 <= max) {
            progress = 1;
        }
    }
</script>
<div class="flex h-full scroll-container" id="scrollContainer" bind:this="{scrollContainer}">
    <div class="w-1/4 absolute mt-10 pl-10">
        <ul class="steps steps-vertical">
            <li class="step step-accent"><a href="#{fileID}">{fileTxt}</a></li>
            {#if progress >= 2}
                <li class="step step-accent"><a href="#{nameID}">{nameTxt}</a></li>
            {:else}
                <li class="step"><a href="#{nameID}">{nameTxt}</a></li>
            {/if}
            {#if progress >= 3}
                <li class="step step-accent"><a href="#{securityID}">{securityTxt}</a></li>
            {:else}
                <li class="step"><a href="#{securityID}">{securityTxt}</a></li>
            {/if}
            {#if progress >= 4}
                <li class="step step-accent"><a href="#{sharingID}">{sharingTxt}</a></li>
            {:else}
                <li class="step"><a href="#{sharingID}">{sharingTxt}</a></li>
            {/if}
        </ul>
    </div>
    <div class="w-1/4"></div>
    <div class="w-3/4">
        <div id="{fileID}" class="pt-10 snap-start" bind:this={scrollPositionItem1}>
            <div class="section-header">{fileTxt}</div>
            <div class="section-content">
                <input type="file" class="file-input file-input-bordered file-input-accent w-full max-w-xs" />
            </div>
        </div>
        <div class="divider"></div>
        <div id="{nameID}" class="pt-10 snap-start" bind:this={scrollPositionItem2}>
            <div class="section-header">{nameTxt}</div>
            <div class="section-content">
                <div class="join">
                    <input type="text" placeholder="Dateiname" class="input input-bordered w-full max-w-xs join-item" />
                    <div class="btn btn-accent join-item">.pdf</div>
                </div>
            </div>
        </div>
        <div class="divider"></div>
        <div id="{securityID}" class="pt-10 snap-start" bind:this={scrollPositionItem3}>
            <div class="section-header">{securityTxt}</div>
            <div class="section-content">
                <div class="flex flex-row items-center mb-8">
                    <span class="mr-2">Datei verschlüsseln</span>
                    <input type="checkbox" checked="checked" class="checkbox checkbox-accent" />
                </div>
                <div class="flex flex-row flex-wrap items-center">
                    <span class="mr-2">Datei mit Passwort schützen</span>
                    <input type="checkbox" class="checkbox checkbox-accent" />
                    <div class="basis-full"></div>
                    <input type="text" placeholder="Passwort" class="input input-bordered w-full max-w-xs mt-4" />
                </div>
            </div>
        </div>
        <div class="divider"></div>
        <div id="{sharingID}" class="pt-10 snap-start" bind:this={scrollPositionItem4}>
            <div class="section-header">{sharingTxt}</div>
            <div class="section-content">
                <div class="flex flex-row flex-wrap items-center">
                    <span>Link</span>
                    <input type="checkbox" class="toggle toggle-accent toggle-md mx-2" />
                    <span>Direkt</span>
                </div>
                <div class="join mt-4 w-full">
                    <input type="text" placeholder="Link" class="input input-bordered w-4/5 join-item" />
                    <button class="btn btn-accent join-item">Kopieren</button>
                </div>
            </div>
        </div>
        <div class="divider"></div>
        <button class="btn btn-outline btn-accent">Datei hochladen</button>
        <div class="h-3/5"></div>
    </div>
</div>