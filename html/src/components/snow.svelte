<div class="snow">●</div>

<style>
    .snow {
        color: snow; /*雪の色*/
        font-size: 18px; /*雪の大きさ*/
        position: fixed;
        top: -5%; /*初期位置*/
        text-shadow:
                5vw   -100px 2px,
                10vw  -400px 3px,
                20vw  -500px 4px,
                30vw  -580px 1px,
                39vw  -250px 2px,
                42vw  -340px 5px,
                56vw  -150px 2px,
                63vw  -180px 0,
                78vw  -220px 4px,
                86vw  -320px 9px,
                94vw  -170px 7px;
        animation: anim 5s linear infinite;
    }

    @keyframes anim {
        100% {
            color: transparent;
            top: 150%;
        }
    }

    :global(body:not(.silent-night)) .snow {
        display: none;
    }
</style>
