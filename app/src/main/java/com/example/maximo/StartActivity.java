package com.example.maximo;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class StartActivity extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(true);
        /* Use skip button behavior */
        setButtonBackFunction(BUTTON_BACK_FUNCTION_SKIP);
        /* Use back button behavior */
        setButtonBackFunction(BUTTON_BACK_FUNCTION_BACK);
        setButtonNextVisible(true);
        /* Use next and finish button behavior */
        setButtonNextFunction(BUTTON_NEXT_FUNCTION_NEXT_FINISH);
        /* Use next button behavior */
//        setButtonNextFunction(BUTTON_NEXT_FUNCTION_NEXT);
        setButtonCtaVisible(false);

        addSlide(new SimpleSlide.Builder()
                .title("Prediction of Investment")
                .description("MAXIMO offers to predict your investment in sectors of STOCK MARKET, GOLD and REAL ESTATE to help you invest wisely.")
                .image(R.drawable.slide1)
                .background(R.color.colorAccent)
                .backgroundDark(R.color.colorPrimary)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Recommendation of Investment")
                .description("MAXIMO presents to you variety of lists of BONDS , MUTUAL FUNDS and LOANS according to your interest to help you gain more.")
                .image(R.drawable.slide2)
                .background(R.color.colorAccent)
                .backgroundDark(R.color.colorPrimary)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Simplifying Finance")
                .description("MAXIMO helps you to build your own customized PORTFOLIO for achieving your dreams and getting you through difficult financial jargons like INCOME TAX etc.")
                .image(R.drawable.slide3)
                .background(R.color.colorAccent)
                .backgroundDark(R.color.colorPrimary)
                .build());
    }

}
