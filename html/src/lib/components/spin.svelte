<script>
    export let spin = undefined
    export let size
</script>

<div
    class="spin {spin.isUp() ? 'spin-up' : 'spin-down'}"
    style="--size: {size}"
>

</div>

<style>
    .spin {
        width: calc(var(--size) * 1px);
        height: calc(var(--size) * 1px);
        border: none;
    }

    .spin-up {
        background-color: #E9EBF7;
    }

    .spin-down {
        background-color: #73747A;
    }

    :global(body.silent-night) .spin-up {
        background-color: #FA4C2F;

    }

    :global(body.silent-night) .spin-down {
        background-color: #07AB65;
    }
</style>
