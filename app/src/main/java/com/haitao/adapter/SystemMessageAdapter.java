package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ColorPhrase;
import com.haitao.utils.TextUtil;

import java.util.List;

import io.swagger.client.model.MsgsListModelDataRows;

/**
 * 系统提醒 - Adapter
 * Created by penley on 16/3/7.
 */
public class SystemMessageAdapter extends BaseListAdapter<MsgsListModelDataRows> {

    public boolean isSystemMessage = true;

    public SystemMessageAdapter(Context context, List<MsgsListModelDataRows> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_system_message, null);
            holder.tvTime = getView(convertView, R.id.tvTime);
            holder.tvContent = getView(convertView, R.id.tvContent);
            holder.divider = getView(convertView, R.id.viewSeparate);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        MsgsListModelDataRows obj = mList.get(position);
        if (obj != null) {
            holder.tvTime.setText(obj.getSentTime());
            String content = String.format("%s        {%s}", obj.getContent(), isSystemMessage ? " " : "查看详情");
            CharSequence format = ColorPhrase.from(TextUtil.getClickableHtml(mContext, content))
                    .withSeparator("{}")
                    .innerColor(mContext.getResources().getColor(R.color.darkOrange))
                    .outerColor(mContext.getResources().getColor(R.color.darkGrey))
                    .format();
            holder.tvContent.setText(format);
            // 最后一条隐藏分割线
            holder.divider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
            /*holder.tvContent.setOnTouchListener((v, event) -> {
                boolean      ret    = false;
                CharSequence text   = ((TextView) v).getText();
                Spannable    stext  = Spannable.Factory.getInstance().newSpannable(text);
                TextView     widget = (TextView) v;
                int          action = event.getAction();

                if (action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= widget.getTotalPaddingLeft();
                    y -= widget.getTotalPaddingTop();

                    x += widget.getScrollX();
                    y += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int    line   = layout.getLineForVertical(y);
                    int    off    = layout.getOffsetForHorizontal(line, x);

                    ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);

                    if (link.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            link[0].onClick(widget);
                        }
                        ret = true;
                    }
                }
                return ret;
            });*/
            /*convertView.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(obj.getJumpType()) && !TextUtils.isEmpty(obj.getJumpValue())) {
                    SlidePicModel slidePicModel = new SlidePicModel();
                    slidePicModel.setType(obj.getJumpType());
                    slidePicModel.setLinkData(obj.getJumpValue());
                    TopicLink.jump(mContext, slidePicModel, "");
                }
            });*/
        }

        return convertView;
    }

    private class Holder {
        TextView tvTime;
        TextView tvContent;
        View     divider;
    }
}
