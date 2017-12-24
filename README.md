# MultipleChoiceProxy
功能说明<br/>
多选代理是用于Android开发中的列表项多选处理的解耦工具，提供零介入实现多选、单选、普通列表界面切换的功能，包括选择结果集的提取<br/><br/>

使用方法<br/>
1.查找ListView控件<br/>
mListView = (ListView) findViewById(R.id.listView);<br/>
2.定义普通的适配器<br/>
mBaseAdapter = new StringAdapter(this, mDates);<br/>
3.定义多选代理<br/>
mMultiChoiceProxy = new MultiChoiceProxy<String>(this);<br/>
<br/>
4.代理ListView 和 Adapter<br/>
multiChoiceProxy.adapterProxy(mBaseAdapter, mListView);//使用默认选择效果的代理适配器<br/>
multiChoiceProxy.adapterProxy(mBaseAdapter, mListView, false);//不使用默认选择效果的代理适配器，返回的View就是mBaseAdapter 生成的View<br/>
<br/>
设置选择状态变化监听器【可选】<br/>
multiChoiceProxy.setOnSelectedChangeListener(this);<br/>
<br/>
恢复保存的状态【可选】<br/>
multiChoiceProxy.restoreState(savedInstanceState);<br/>
保存适配器状态【可选】<br/>
multiChoiceProxy.saveState(outState);<br/>
<br/>
设置多选模式【可选】<br/>
multiChoiceProxy.setMultiMode(SelectMode.Multi);<br/>
设置单选模式【可选】<br/>
multiChoiceProxy.setMultiMode(SelectMode.Single);<br/>
关闭选择模式【可选】<br/>
multiChoiceProxy.setMultiMode(SelectMode.NONE);<br/>
<br/>
全选<br/>
multiChoiceProxy.selectAll();<br/>
选择前5项<br/>
multiChoiceProxy.selectCount(5);<br/>
清空选择项<br/>
multiChoiceProxy.cancelAll();<br/>
获取已经选择的数据【可选】<br/>
List<String> items = multiChoiceProxy.getSelectedItems();<br/>
获取已经选择的数据,按照列表顺序【可选】<br/>
List<String> items = multiChoiceProxy.getOrderedSelectedItems();<br/>
获取已经选择的个数【可选】<br/>
multiChoiceProxy.getCheckedItemCount();<br/>
是否是选择模式
multiChoiceProxy.isSelectFlag();<br/>
第6项是否被选中
multiChoiceProxy.isChecked(5)；<br/>
