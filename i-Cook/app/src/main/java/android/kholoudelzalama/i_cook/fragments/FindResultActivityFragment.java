package android.kholoudelzalama.i_cook.fragments;

import android.content.Intent;
import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.adapters.HomeAdapter;
import android.kholoudelzalama.i_cook.objects.Recipes;
import android.kholoudelzalama.i_cook.utilities.NetworkUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class FindResultActivityFragment extends Fragment {

    public static final int PAGE_SIZE = 40;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private boolean isLoading = false;


    Gson gson;
    Recipes recipes;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private HomeAdapter adapter;



    public FindResultActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_find_result, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        Intent intent = getActivity().getIntent();
        gson = new Gson();
        final String query =  getArguments().getString(getString(R.string.query_extra));
        getActivity().setTitle(query);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(true);
                        currentPage=1;
                        new SearchResult().execute(query);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        isLoading = true;
                        new SearchResult().execute(query);
                        currentPage += 1;
                        progressBar.setVisibility(View.VISIBLE);



                    }
                }
            }
        });
        new SearchResult().execute(query);
        return rootView;
    }

    class SearchResult extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                String query = strings[0];
                int from = PAGE_SIZE *(currentPage-1);
                int to = from + PAGE_SIZE;
                URL url = NetworkUtils.buildUrl(query, String.valueOf(from) , String.valueOf(to));
                String result = NetworkUtils.getResponseFromHttpUrl(url);

                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            swipeRefreshLayout.setRefreshing(false);
            if (null == s) {
                Toast.makeText(getContext(), getString(R.string.api_limits), Toast.LENGTH_LONG).show();

            } else {
                recipes = gson.fromJson(s, Recipes.class);
                adapter=new HomeAdapter(getContext(), recipes);
                recyclerView.setAdapter(adapter);
                if(recipes.getHits().size()==0){
                    isLastPage = true;
                }
                if (recipes.getHits().size()==0 && currentPage ==1){
                    Toast.makeText(getContext(), getString(R.string.no_result), Toast.LENGTH_LONG).show();
                    isLastPage = true;
                }
                if(currentPage>1){
                    progressBar.setVisibility(View.INVISIBLE);
                    isLoading=false;
                    adapter.notifyDataSetChanged();
                }

            }
        }
    }
}
