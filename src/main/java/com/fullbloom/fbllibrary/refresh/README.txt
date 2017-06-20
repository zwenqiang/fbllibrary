

LinearLayoutManager layoutManager = new LinearLayoutManager(this);
layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
mRecyclerView.setLayoutManager(layoutManager);

mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);


// 进入页面自动刷新
mRecyclerView.refresh();


使用 鸿阳 的commentAdapter
'com.zhy:base-rvadapter:3.0.3'
