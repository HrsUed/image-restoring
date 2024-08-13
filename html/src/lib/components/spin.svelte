<script lang="ts">
  import type IsingSpin from '$lib/modules/isingSpin';

  export let spin: IsingSpin;
  export let size: number;
</script>

<div class="spin {spin.isUp() ? 'spin-up' : 'spin-down'}" style="--size: {size}"></div>

<style>
  .spin {
    width: calc(var(--size) * 1px);
    height: calc(var(--size) * 1px);
    border: none;
  }

  .spin-up {
    background-color: #e9ebf7;
  }

  .spin-down {
    background-color: #73747a;
  }

  :global(body.silent-night) .spin-up {
    background-color: #fa4c2f;
  }

  :global(body.silent-night) .spin-down {
    background-color: #07ab65;
  }
</style>
