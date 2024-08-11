<script lang="ts">
  import type BinaryImage from '$lib/modules/binaryImage';
  import Spin from './spin.svelte';

  export let image: BinaryImage;
  export let siteSize = 5;
  export let width = 30;
  export let height = 30;
</script>

<div
  class="image"
  style="--site-size: {siteSize}; --image-width: {width}; --image-height: {height}"
>
  {#each image.sites as site}
    <Spin spin={site} size={siteSize} />
  {/each}
</div>

<style>
  .image {
    width: calc(var(--site-size) * var(--image-width) * 1px);
    height: calc(var(--site-size) * var(--image-height) * 1px);
    border: 1px solid #cccccc;
    margin: 10px 20px;
    display: flex;
    flex-wrap: wrap;
    box-shadow: 5px 5px 5px 1px rgba(0, 0, 0, 0.1);
  }

  :global(body.silent-night) .image {
    box-shadow:
      0 8px 10px 1px rgba(255, 255, 255, 0.15),
      0 3px 15px 2px rgba(255, 255, 255, 0.1),
      0 5px 5px -3px rgba(255, 255, 255, 0.2);
  }
</style>
