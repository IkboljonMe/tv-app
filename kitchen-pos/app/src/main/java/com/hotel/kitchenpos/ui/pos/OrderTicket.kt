package com.hotel.kitchenpos.ui.pos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hotel.kitchenpos.data.Format
import com.hotel.kitchenpos.data.OrderDTO
import com.hotel.kitchenpos.data.OrderStatus
import com.hotel.kitchenpos.data.next
import com.hotel.kitchenpos.ui.theme.Palette

// Uzbek "advance" labels per status, matching the web POS.
private val ADVANCE_LABEL = mapOf(
    OrderStatus.PENDING to "Tayyorlashni boshlash",
    OrderStatus.PREPARING to "Tayyor deb belgilash",
    OrderStatus.READY to "Yetkazildi deb belgilash",
)

@Composable
fun OrderTicket(
    order: OrderDTO,
    onUpdateStatus: (String, OrderStatus) -> Unit,
) {
    val status = order.orderStatus
    val waited = Format.minutesAgo(order.createdAt)
    val urgent = status != OrderStatus.READY && waited >= 15
    val next = status.next

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Palette.White)
            .then(
                if (urgent) Modifier.border(2.dp, Palette.Rose400, RoundedCornerShape(12.dp))
                else Modifier
            )
            .padding(16.dp),
    ) {
        // Header: room number + short id, waited time.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = Palette.Brand600,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = order.roomNumber,
                    color = Palette.Slate900,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "#${order.id.takeLast(6).uppercase()}",
                    color = Palette.Slate400,
                    fontSize = 11.sp,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = if (urgent) Palette.Rose600 else Palette.Slate400,
                    modifier = Modifier.size(13.dp),
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "$waited daq",
                    color = if (urgent) Palette.Rose600 else Palette.Slate400,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Item lines.
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            order.items.forEach { item ->
                Row {
                    Text(
                        text = "${item.quantity}×",
                        color = Palette.Brand600,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = item.name,
                        color = Palette.Slate700,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        if (order.note.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "📝 ${order.note}",
                color = Palette.Amber800,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Palette.Amber50)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
            )
        }

        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Palette.Slate100),
        )
        Spacer(Modifier.height(10.dp))

        // Footer: time · price, and action buttons.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${Format.time(order.createdAt)} · ${Format.price(order.total)}",
                color = Palette.Slate400,
                fontSize = 11.sp,
                modifier = Modifier.weight(1f, fill = false),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (status == OrderStatus.PENDING) {
                    TicketTextButton(
                        label = "Bekor qilish",
                        color = Palette.Rose600,
                        onClick = { onUpdateStatus(order.id, OrderStatus.CANCELLED) },
                    )
                }
                if (next != null) {
                    AdvanceButton(
                        label = ADVANCE_LABEL[status] ?: "Keyingi",
                        onClick = { onUpdateStatus(order.id, next) },
                    )
                }
            }
        }
    }
}

@Composable
private fun TicketTextButton(label: String, color: androidx.compose.ui.graphics.Color, onClick: () -> Unit) {
    Text(
        text = label,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    )
}

@Composable
private fun AdvanceButton(label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Palette.Slate900)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = Palette.White,
            modifier = Modifier.size(14.dp),
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = label,
            color = Palette.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
