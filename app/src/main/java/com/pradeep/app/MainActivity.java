package com.pradeep.app;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import android.arch.lifecycle.Observer;

import com.pradeep.app.databinding.AddEditUserBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private TextView mLocalUserList, mRemoteUserList;
    private RecyclerView mUserView;
    private ImageButton mAddUser;
    private Activity mActivity;
    private Dialog myDialog;
    private Context mContext;
    private AddUserData addUserData;
    private Dialog mDailog;
    private List mUserList;
    private UserListAdaptor mAdaptor;
    private int mMode = -1;
    private TextView mLoadMoreUser;
    private boolean mLeftLoadMoreUser;
    private boolean mEditAddAction = true;
    private int mUserIdEdit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserList = new ArrayList();
        mLocalUserList = (TextView) findViewById(R.id.locallist);
        mRemoteUserList = (TextView) findViewById(R.id.remotelist);
        mUserView = (RecyclerView) findViewById(R.id.userList);
        mAddUser = (ImageButton) findViewById(R.id.add_user);
        mLoadMoreUser = (TextView) findViewById(R.id.remoteLoadMore) ;
        mContext = this;
        mLeftLoadMoreUser = true;
        mActivity = this;
        addUserData = ViewModelProviders.of(this).get(AddUserData.class);
        mLocalUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMode != 0) {
                    mUserView.setVisibility(View.VISIBLE);
                    mAddUser.setVisibility(View.VISIBLE);
                    mUserList = new ArrayList();
                    getLocalUserList();
                    mLeftLoadMoreUser = true;
                    mLoadMoreUser.setVisibility(View.GONE);
                    mRemoteUserList.setBackgroundColor(0xFFFFFF);
                    mRemoteUserList.setBackgroundResource(R.drawable.rect);
                    mLocalUserList.setBackgroundColor(0xFF87ceeb);
                    mMode = 0;
                }
            }
        });
        mAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditAddAction = true;
                addUserData.setDefault();
                showDailog();
            }
        });
        mRemoteUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMode != 1) {
                    mLoadMoreUser.setText("loard more");
                    mUserView.setVisibility(View.GONE);
                    mUserList = new ArrayList();
                    mLocalUserList.setBackgroundColor(0xFFFFFF);
                    mLocalUserList.setBackgroundResource(R.drawable.rect);
                    mRemoteUserList.setBackgroundColor(0xFF87ceeb);
                    mAddUser.setVisibility(View.GONE);
                    mLoadMoreUser.setVisibility(View.VISIBLE);
                    mLoadMoreUser.setPaintFlags(mLoadMoreUser.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                    getRemoteData();
                    mMode = 1;
                }
            }
        });

        mLoadMoreUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLeftLoadMoreUser == true) {
                    getApi(2);
                    mLeftLoadMoreUser = false;
                    mLoadMoreUser.setText("no more data");
                }
            }
        });
        addUserData.getUser().observe(this, new Observer<UserDetails>() {
            @Override
            public void onChanged(@Nullable UserDetails loginUser) {
                if(mEditAddAction == true) {
                    saveTask(loginUser.getFirstName(), loginUser.getLastName(), loginUser.getEmailId());
                } else {
                    User data = new User();
                    data.setFirstName(loginUser.getFirstName());
                    data.setLastName(loginUser.getLastName());
                    data.setEmailId(loginUser.getEmailId());
                    updateTask(data);
                }
                mDailog.dismiss();
            }
        });

    }

    private void showDailog() {
        mDailog = new Dialog(mContext, R.style.full_screen_dialog);
        AddEditUserBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.add_edit_user, null, false);
        mDailog.setContentView(binding.getRoot());
        binding.setViewModel(addUserData);
        ImageButton data = (ImageButton) mDailog.findViewById(R.id.close_page);
        data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDailog.dismiss();
            }
        });
        mDailog.show();
    }

    private void saveTask(final String firstName, final String lastName, final String email) {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                User data = new User();
                Log.e("TAG", firstName + lastName + email);
                data.setFirstName(firstName);
                data.setLastName(lastName);
                data.setEmailId(email);
                UserDatabseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .localUserDao()
                        .insert(data);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                fetchList();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    private void getLocalUserList() {
        mAdaptor = new UserListAdaptor(0, mUserList, new ClickListioner() {
            @Override
            public void onItemClick(View v, List<User> list, int position, int type) {
                if (type == 0) {
                    User get = list.get(position);
                    addUserData.setUserData(get);
                    mEditAddAction = false;
                    mUserIdEdit = get.getId();
                    Log.e("TAG","datads......."+ get.getId());
                    showDailog();
                } else if (type == 1) {
                    deleteTask(list.get(position));
                    list.remove(position);
                    mAdaptor.changeList(0,list);
                }
            }
        });
        mUserView.setHasFixedSize(true);
        mUserView.setLayoutManager(new LinearLayoutManager(mContext));
        mUserView.setAdapter(mAdaptor);
        fetchList();
    }

    private void fetchList() {
        Log.e("TAG","datads");
        class getUserList extends AsyncTask<Void, Void, List<User>> {
            @Override
            protected List<User> doInBackground(Void... voids) {
                List<User> taskList = UserDatabseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .localUserDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<User> tasks) {
                super.onPostExecute(tasks);

                Log.e("TAG","datads"+tasks.size());
                Log.e("TAG","datads:-----"+tasks.get(0).getId()+"    "+tasks.get(1).getId());
                mUserList = tasks;
                mAdaptor.changeList(0,tasks);
            }
        }
        getUserList gt = new getUserList();
        gt.execute();
    }

    private void deleteTask(final User task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                UserDatabseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .localUserDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
            }
        }
        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

    private void updateTask(final User task) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.e("TAG","datads......."+ task.getId()+"     "+mUserIdEdit);
                UserDatabseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .localUserDao()
                        .update(task.getFirstName(),task.getLastName(),task.getEmailId(),mUserIdEdit);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                fetchList();
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
            }
        }
        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    private void getRemoteData() {
        mAdaptor = new UserListAdaptor(1, mUserList, new ClickListioner() {
            @Override
            public void onItemClick(View v, List<User> list, int position, int type) {

            }
        });
        mUserView.setHasFixedSize(true);
        mUserView.setLayoutManager(new LinearLayoutManager(mContext));
        mUserView.setAdapter(mAdaptor);
        mUserView.setVisibility(View.VISIBLE);
        getApi(1);
    }

    private void getApi(int page) {
        Call<AUserList> call = RemoteFetch.getInstance().getMyApi().getUserList(page);
        call.enqueue(new Callback<AUserList>() {
            @Override
            public void onResponse(Call <AUserList> call, Response<AUserList> response) {
                AUserList remoteuser = response.body();
                Log.e("TAG",remoteuser.data+"");
                List data = mUserList;
                mUserList = new ArrayList();
                mUserList.addAll(data);
                mUserList.addAll(remoteuser.data);
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mAdaptor.changeList(1, mUserList);
                    }
                });
            }

            @Override
            public void onFailure(Call<AUserList> call, Throwable t) {
                Log.e("TAG",""+t.toString());
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

}