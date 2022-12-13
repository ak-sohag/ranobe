package org.ranobe.ranobe.ui.details.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.ranobe.ranobe.models.Novel;
import org.ranobe.ranobe.repository.Repository;
import org.ranobe.ranobe.sources.Source;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailsViewModel extends ViewModel {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Novel> details;
    private String oldUrl = "";

    public MutableLiveData<Novel> getDetails(String novelUrl) {
        if (details == null || !oldUrl.equals(novelUrl)) {
            oldUrl = novelUrl;
            details = new MutableLiveData<>();
        }
        return details;
    }

    public void details(Source source, String novelUrl) {
        Repository repository = new Repository(executor, source);
        repository.details(novelUrl, new Repository.Callback<Novel>() {
            @Override
            public void onComplete(Novel result) {
                details.postValue(result);
            }

            @Override
            public void onError(Exception e) {
                error.postValue(e.getLocalizedMessage());
            }
        });
    }
}
