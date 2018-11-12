package sussex.android.ase_android.MapsScreen.BottomSheet;


import sussex.android.ase_android.BasePresenter;

public interface BottomSheetContract {
        interface View {
                int getPeekHeightPx();
                void collapseBottomSheet();
                void hideBottomSheet();
        }

        interface Presenter extends BasePresenter {

        }
}
