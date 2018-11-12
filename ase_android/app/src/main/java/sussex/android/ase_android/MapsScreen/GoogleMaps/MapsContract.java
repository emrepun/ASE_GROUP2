package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.app.Activity;

import sussex.android.ase_android.BasePresenter;

public interface MapsContract {
    interface View {
        Activity getActivity();
    }

    interface Presenter extends BasePresenter {
    }
}
